package com.example.rail.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Name: com.example.rail.config
 * @Description:
 * @Auther: zhzy
 * @Date: 2019-07-2019/07/25 17:40
 */
@Configuration
public class RedisFactoryConfig {

    @Autowired
    private Environment environment;
    @Bean
    public RedisConnectionFactory myLettuceConnectionFactory() {
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("spring.redis.cluster.nodes", environment.getProperty("spring.redis.cluster-nodes"));
        source.put("spring.redis.cluster.timeout", environment.getProperty("spring.redis.command-timeout"));
        source.put("spring.redis.cluster.max-redirects", 300);
        RedisClusterConfiguration redisClusterConfiguration;
        redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
        return new LettuceConnectionFactory(redisClusterConfiguration);
    }
}
