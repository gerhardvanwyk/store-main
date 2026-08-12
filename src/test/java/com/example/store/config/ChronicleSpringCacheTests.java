package com.example.store.config;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ChronicleSpringCacheTests {

    private ChronicleSpringCache cache;
    private ChronicleMap<Long, Serializable> chronicleMap;

    @BeforeEach
    void setUp() {
        chronicleMap = ChronicleMapBuilder
                .of(Long.class, Serializable.class)
                .name("test-map")
                .entries(10)
                .averageValue("test")
                .create();
        cache = new ChronicleSpringCache("test-cache", chronicleMap);
    }

    @AfterEach
    void tearDown() {
        chronicleMap.close();
    }

    @Test
    void testGetName() {
        assertEquals("test-cache", cache.getName());
    }

    @Test
    void testPutAndGet() {
        cache.put(1L, "value");
        assertEquals("value", cache.get(1L).get());
    }

    @Test
    void testPutInvalidKeyType() {
        assertThrows(IllegalArgumentException.class, () -> cache.put("invalid", "value"));
    }

    @Test
    void testPutInvalidValueType() {
        assertThrows(IllegalArgumentException.class, () -> cache.put(1L, new Object()));
    }

    @Test
    void testEvict() {
        cache.put(1L, "value");
        cache.evict(1L);
        assertNull(cache.get(1L));
    }

    @Test
    void testClear() {
        cache.put(1L, "value1");
        cache.put(2L, "value2");
        cache.clear();
        assertNull(cache.get(1L));
        assertNull(cache.get(2L));
    }

    @Test
    void testGetWithValueLoader() {
        AtomicInteger counter = new AtomicInteger(0);
        String value = cache.get(1L, () -> {
            counter.incrementAndGet();
            return "loaded";
        });
        assertEquals("loaded", value);
        assertEquals(1, counter.get());

        // Second call should return cached value
        String value2 = cache.get(1L, () -> {
            counter.incrementAndGet();
            return "loaded again";
        });
        assertEquals("loaded", value2);
        assertEquals(1, counter.get());
    }

    @Test
    void testGetWithType() {
        cache.put(1L, "value");
        assertEquals("value", cache.get(1L, String.class));
    }

    @Test
    void testGetWithWrongType() {
        cache.put(1L, "value");
        assertThrows(IllegalStateException.class, () -> cache.get(1L, Integer.class));
    }
}
