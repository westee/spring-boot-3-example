package com.westee.consumer;

import com.westee.rpc.OrderRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @DubboReference
    private OrderRpcService orderRpcService;

    public void hello() {
        orderRpcService.hello();
    }
}
