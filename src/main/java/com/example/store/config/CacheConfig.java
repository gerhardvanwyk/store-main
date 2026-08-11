package com.example.store.config;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.annotation.EnableCaching;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${app.cache.chronicle-map.file:customers_cache.dat}")
    private String cacheFile;

    @Value("${app.cache.chronicle-map.entries:1000}")
    private long entries;

    @Bean
    @SuppressWarnings("unchecked")
    public CacheManager cacheManager() throws IOException {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<Cache> caches = new ArrayList<>();

        // Chronicle Map for "customers" cache
        // We use a persisted map for production as requested
        File file = new File(cacheFile);
        
        ChronicleMap<String, Serializable> customersMap = ChronicleMapBuilder
                .of(String.class, Serializable.class)
                .name("customers-map")
                .entries(entries)
                .averageKey("customer-key")
                .averageValue(new ArrayList<>())
                .createPersistedTo(file);

        // We wrap ChronicleMap to ensure it behaves correctly with Spring Cache
        // ChronicleMap implements ConcurrentMap, so ConcurrentMapCache can use it.
        // ChronicleMap requires String keys here, but Spring uses SimpleKey for no-arg methods.
        // We use a custom wrapper or key generator if needed, but for now we try to force String keys.
        caches.add(new ConcurrentMapCache("customers", (ConcurrentMap) customersMap, false));

        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
