package com.westee.provider;

import com.westee.rpc.OrderRpcService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class OrderServiceImpl implements OrderRpcService {
    @Override
    public void hello() {
        System.out.println("hello");
    }
}
