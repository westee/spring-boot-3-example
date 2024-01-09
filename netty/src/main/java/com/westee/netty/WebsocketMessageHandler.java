package com.westee.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@ChannelHandler.Sharable
@Component
public class WebsocketMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketMessageHandler.class);
    private static final ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final HashMap<String, Object> STRING_OBJECT_HASH_MAP = new HashMap<String, Object>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ConcurrentSkipListSet<String> userSet = new ConcurrentSkipListSet<>();

    @Autowired
    NService nService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws JsonProcessingException {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
            HashMap<String, Object> hashMap = null;
            try {
                hashMap = objectMapper.readValue(textWebSocketFrame.text(), new TypeReference<HashMap<String, Object>>() {
                });
            } catch (JsonProcessingException e) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame(generateResponse("发送失败")));
            }
            if (hashMap != null && hashMap.get("type") != null && hashMap.get("type").toString().equals("login")) {
                ctx.channel().attr(AttributeKey.valueOf("user")).set(hashMap.get("username"));
                userSet.add(hashMap.get("username").toString());
                ctx.channel().writeAndFlush(new TextWebSocketFrame("{\"type\": \"userList\", \"message\":" + objectMapper.writeValueAsString(userSet) + "}"));
            }
            // 业务层处理数据
            this.nService.discard(textWebSocketFrame.text());
            // 响应客户端
            ctx.channel().writeAndFlush(new TextWebSocketFrame(generateResponse("发送成功")));
            allChannels.writeAndFlush(textWebSocketFrame.retain()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            // 不接受文本以外的数据帧类型
            ctx.channel().writeAndFlush(WebSocketCloseStatus.INVALID_MESSAGE_TYPE).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        allChannels.remove(ctx.channel());
        String s = (String) ctx.channel().attr(AttributeKey.valueOf("user")).get();
        allChannels.writeAndFlush(new TextWebSocketFrame("{\"type\": \"logout\", \"username\":" + "\"" + s + "\"" + "}"));
        userSet.remove(s);
        LOGGER.info("链接断开：{}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        allChannels.add(ctx.channel());
        LOGGER.info("链接创建：{}", ctx.channel().remoteAddress());
    }

    public String generateResponse(String message) {
        STRING_OBJECT_HASH_MAP.put("status", message);
        try {
            return objectMapper.writeValueAsString(STRING_OBJECT_HASH_MAP);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
