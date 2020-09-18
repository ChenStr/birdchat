package com.netty.websocket;

import com.netty.websocket.netty.websocket.WebSocketCreate;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class WebSocketRunServer implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {
                WebSocketCreate.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
