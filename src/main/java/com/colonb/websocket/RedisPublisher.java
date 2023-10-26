package com.colonb.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, ChatMessage> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message) {
        System.out.println("publish");
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

}
