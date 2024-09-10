package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("start creating redis model object...");
        RedisTemplate redisTemplate = new RedisTemplate();
        //setup redis connection factory object
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //set redis key serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
