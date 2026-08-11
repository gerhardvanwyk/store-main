package com.example.store.config;

import net.openhft.chronicle.map.ChronicleMap;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class ChronicleSpringCache implements Cache {

    private final String name;
    private final ChronicleMap<Long, Serializable> chronicleMap;

    public ChronicleSpringCache(String name, ChronicleMap<Long, Serializable> chronicleMap) {
        this.name = name;
        this.chronicleMap = chronicleMap;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.chronicleMap;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = chronicleMap.get(key);
        return (value != null) ? new SimpleValueWrapper(value) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = chronicleMap.get(key);
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of type " + type.getName());
        }
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        // Simple synchronized fallback if the entry is missing
        if (chronicleMap.containsKey(key)) {
            return (T) chronicleMap.get(key);
        }
        synchronized (this) {
            if (chronicleMap.containsKey(key)) {
                return (T) chronicleMap.get(key);
            }
            try {
                T value = valueLoader.call();
                put(key, value);
                return value;
            } catch (Exception e) {
                throw new ValueRetrievalException(key, valueLoader, e);
            }
        }
    }

    @Override
    public void put(Object key, Object value) {
        validateCacheEntry(key, value);

        Long cacheKey = (Long) key;
        Serializable cacheValue = (Serializable) value;

        chronicleMap.put(cacheKey, cacheValue);
    }

    @Override
    public void evict(Object key) {
        chronicleMap.remove(key);
    }

    @Override
    public void clear() {
        chronicleMap.clear();
    }

    private void validateCacheEntry(Object key, Object value) {
        if (!(key instanceof Long) || !(value instanceof Serializable)) {
            throw new IllegalArgumentException("Key and value must be of type Long and Serializable respectively");
        }
    }

    private void validCacheKey(Object key) {
        if (!(key instanceof Long))  {
            throw new IllegalArgumentException("Key must be of type Long");
        }
    }
 }

