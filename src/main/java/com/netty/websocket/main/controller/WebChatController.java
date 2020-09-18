package com.netty.websocket.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/webchat")
@Controller
public class WebChatController {

    /**
     * 连接WebSocket
     */
    @GetMapping("/test")
    public String getWebSocket(){
        return "test";
    }

    @GetMapping("/test123")
    public String Test123(){
        return "/pages/userList";
    }

}
