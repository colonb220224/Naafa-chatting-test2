package com.colonb.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class RedisConfig {
    private RedisServer redisServer;

    public RedisConfig(@Value("${spring.redis.port}") int port) throws IOException {
        this.redisServer = RedisServer.builder().port(port).setting("maxmemory 128M").build();
    }

    @PostConstruct
    public void startRedis() {
        this.redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        this.redisServer.stop();
    }
}
