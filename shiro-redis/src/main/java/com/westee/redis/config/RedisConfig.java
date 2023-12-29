package com.westee.redis.config;

import com.westee.redis.serializer.ObjectSerializer;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        val template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);

        val genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashKeySerializer(genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.setDefaultSerializer(genericJackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public RedisTemplate<String, Object> sessionRedisTemplate(RedisConnectionFactory factory) {
        val template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);

        val genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new ObjectSerializer());
        template.setHashKeySerializer(genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.setDefaultSerializer(genericJackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }
}
