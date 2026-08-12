package com.example.store.config;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.EnableCaching;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${app.cache.chronicle-map.customers:customers_cache.dat}")
    private String customerCache;

    @Value("${app.cache.chronicle-map.products:products_cache.dat}")
    private String productsCache;

    @Value("${app.cache.chronicle-map.orders:oders_cache.dat}")
    private String ordersCache;

    @Value("${app.cache.chronicle-map.entries:1000}")
    private long entries;

    @Bean
    @SuppressWarnings("unchecked")
    public CacheManager cacheManager() throws IOException {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<Cache> caches = new ArrayList<>();

        File ordersFile = new File(ordersCache);
        ChronicleMap<Long, Serializable> ordersMap = ChronicleMapBuilder
                .of(Long.class, Serializable.class)
                .name("orders-map")
                .entries(entries)
                .averageValue(new String())
                .createPersistedTo(ordersFile);

        ChronicleSpringCache springOrdersCache = new ChronicleSpringCache("orders", ordersMap);
        caches.add(springOrdersCache);

        // Chronicle Map for "customers" cache
        File customersFile = new File(customerCache);
        ChronicleMap<Long, Serializable> customersMap = ChronicleMapBuilder
                .of(Long.class, Serializable.class)
                .name("customers-map")
                .entries(entries)
                .averageValue(new String())
                .createPersistedTo(customersFile);

        // Wrap it inside your custom Spring Cache adapter
        ChronicleSpringCache springCustomerCache = new ChronicleSpringCache("customers", customersMap);
        caches.add(springCustomerCache);

        // Chronicle Map for "products" cache
        File productsFile = new File(productsCache);
        ChronicleMap<Long, Serializable> productsMap = ChronicleMapBuilder
                .of(Long.class, Serializable.class)
                .name("products-map")
                .entries(entries)
                .averageValue(new String())
                .createPersistedTo(productsFile);

        ChronicleSpringCache springProductsCache = new ChronicleSpringCache("products", productsMap);
        caches.add(springProductsCache);

        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
