package com.interestconnect.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig implements CachingConfigurer {

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration cacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10))
                        .disableCachingNullValues()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(
                                                new GenericJackson2JsonRedisSerializer()
                                        )
                        );

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    @Override
    @Bean
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler();
    }

    private static class RedisCacheErrorHandler
            extends SimpleCacheErrorHandler {

        @Override
        public void handleCacheGetError(
                RuntimeException exception,
                org.springframework.cache.Cache cache,
                Object key) {

            System.err.println(
                    "Redis GET failed for cache '"
                            + cache.getName()
                            + "', key '"
                            + key
                            + "'. Falling back to database."
            );

            // Do NOT throw.
            // Spring will execute the underlying @Cacheable method.
        }

        @Override
        public void handleCachePutError(
                RuntimeException exception,
                org.springframework.cache.Cache cache,
                Object key,
                Object value) {

            System.err.println(
                    "Redis PUT failed for cache '"
                            + cache.getName()
                            + "', key '"
                            + key
                            + "'. Continuing without cache."
            );

            // Don't throw.
        }

        @Override
        public void handleCacheEvictError(
                RuntimeException exception,
                org.springframework.cache.Cache cache,
                Object key) {

            System.err.println(
                    "Redis EVICT failed for cache '"
                            + cache.getName()
                            + "', key '"
                            + key
                            + "'. Continuing."
            );

            // Don't throw.
        }

        @Override
        public void handleCacheClearError(
                RuntimeException exception,
                org.springframework.cache.Cache cache) {

            System.err.println(
                    "Redis CLEAR failed for cache '"
                            + cache.getName()
                            + "'. Continuing."
            );

            // Don't throw.
        }
    }
}
