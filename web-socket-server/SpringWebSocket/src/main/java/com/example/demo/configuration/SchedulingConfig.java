package com.example.demo.configuration;

import com.example.demo.model.Message;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@AllArgsConstructor
public class SchedulingConfig {

    private SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(fixedDelay = 3000)
    public void sendMessage() {
        simpMessagingTemplate.convertAndSend("/topic/hello", new Message("Hi all"));
    }
}
