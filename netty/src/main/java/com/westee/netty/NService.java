package com.westee.netty;

import org.springframework.stereotype.Component;

@Component
public class NService {

    public void discard (String message) {
        System.out.println("message");
        System.out.println(message);
    }
}
