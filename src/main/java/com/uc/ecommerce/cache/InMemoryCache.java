package com.uc.ecommerce.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存缓存，替代 Redis。
 * 生产环境用 Redis 做：分布式锁、库存缓存、幂等防重。
 * demo 里业务正确性靠数据库乐观锁 + 唯一约束保证，缓存不强制参与，这里留接口占位。
 */
@Component
public class InMemoryCache {

    private final ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

    public void set(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public void delete(String key) {
        map.remove(key);
    }
}
