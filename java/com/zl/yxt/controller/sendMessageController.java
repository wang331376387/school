package com.zl.yxt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/websocket")
public class sendMessageController {

    @Autowired
    private WebSocket webSocket;

    @GetMapping("/sendAllWebSocket")
    public String test() {
        String text="你们好！这是websocket群体发送！";
        webSocket.sendAllMessage(text);
        return text;
    }

    @GetMapping("/sendOneWebSocket/{userName}")
    public String sendOneWebSocket(@PathVariable("userName") String userName) {
        String text=userName+" 你好！ 这是websocket单人发送！";
        webSocket.sendOneMessage(userName,text);
        return text;
    }
    @GetMapping("/send/{userName}")
    public String sendOneWebSocket2(@PathVariable("userName") String userName) {
        UUID uuid = UUID.randomUUID();
        String text=userName+" 你好！ 这是websocket单人发送！"+uuid;
        webSocket.sendOneMessage(userName,text);
        return text;
    }
}
