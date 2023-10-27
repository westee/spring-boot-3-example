package com.westee.shiro.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.westee.shiro.exception.HttpException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
public class UserService {
    String APPID = "appid";
    String SECRET = "appid";
    public WeChatSession getWeChatSession(String wxcode) throws JsonProcessingException {
        WeChatSession weChatSession = new WeChatSession();
        weChatSession.setOpenid("openid");
        return weChatSession;
//        RestTemplate restTemplate = new RestTemplate();
//        String resourceURL = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APPID +
//                "&secret=" + SECRET + "&js_code=" + wxcode + "&grant_type=authorization_code";
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange(resourceURL, HttpMethod.GET, null, String.class);
//        System.out.println(responseEntity);
//        WeChatSession weChatSession = null;
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            String sessionData = responseEntity.getBody();
//            //解析从微信服务器获得的openid和session_key;
//            ObjectMapper objectMapper = new ObjectMapper();
//            weChatSession = objectMapper.readValue(sessionData, WeChatSession.class);
//
//            if (Objects.nonNull(weChatSession.getOpenid())) {
//                // 一些操作
//            }
//
//            //获取用户的唯一标识
//            String openid = weChatSession.getOpenid();
//
//            //获取会话秘钥
//            String session_key = weChatSession.getSession_key();
//            String unionid = weChatSession.getUnionid();
//
//            return weChatSession;
//        }
//        throw HttpException.badRequest(responseEntity.getBody());
    }

    public static class WeChatSession {
        private Integer errcode;
        private String errmsg;
        private String openid;
        private String session_key;
        private String unionid;

        public Integer getErrcode() {
            return errcode;
        }

        public void setErrcode(Integer errcode) {
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getSession_key() {
            return session_key;
        }

        public void setSession_key(String session_key) {
            this.session_key = session_key;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }
    }
}
