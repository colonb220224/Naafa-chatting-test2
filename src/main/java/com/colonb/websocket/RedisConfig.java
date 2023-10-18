package com.colonb.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
   @Bean
    public RedisConnectionFactory redisConnectionFactory(){
       return new LettuceConnectionFactory("localhost", 6379);
   }

    // ChannelTopic 등록
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatroom");
    }

//    /**
//     * redis pub/sub 메시지를 처리하는 listener 설정
//     */
//    @Bean
//    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        return container;
//    }

    // MessageListenerAdapter 등록
    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "sendMessage");
    }

    // 등록한 ChannelTopic, MessageListenerAdapter로 RedisMessageListenerContainer 등록
    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory,
                                                              MessageListenerAdapter listenerAdapter,
                                                              ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    /**
     * 어플리케이션에서 사용할 redisTemplate 설정
     */
    @Bean
    public RedisTemplate<String, ChatMessage> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ChatMessage> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory); // connection
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class)); // value
        return redisTemplate;
    }

}
