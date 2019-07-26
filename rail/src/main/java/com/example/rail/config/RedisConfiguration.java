package com.example.rail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @Name: com.example.rail.config
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/25 17:39
 */
@Configuration
public class RedisConfiguration {
    @Resource
    private LettuceConnectionFactory myLettuceConnectionFactory;
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate() {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(myLettuceConnectionFactory);
        return template;
    }
}
