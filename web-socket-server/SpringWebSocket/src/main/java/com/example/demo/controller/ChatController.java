package com.example.demo.controller;

import com.example.demo.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    @MessageMapping("/hello")
    @SendTo("/topic/hello")
    public Message send(Message message) {
        System.out.println("message: " + message.toString());
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new Message(message.getFrom(), message.getText(), time);
    }
}
