package com.Ecommerce.Ecommerce.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("carrinho") {
            protected CacheBuilder<Object, Object> createNativeCacheBuilder() {
                return CacheBuilder.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES);
            }
        };
    }
}
