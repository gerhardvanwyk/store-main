package com.example.store.controller;

import com.example.store.config.CacheConfig;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.entity.Customer;
import com.example.store.mapper.OrderMapper;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = IntegrationTest.TestConfig.class, properties = {
        "app.cache.chronicle-map.customers=build/test-customers-2.dat",
        "app.cache.chronicle-map.products=build/test-products-2.dat",
        "app.cache.chronicle-map.orders=build/test-orders-2.dat"
})
public class IntegrationTest {

    @Configuration
    @EnableCaching
    @Import({ProductController.class, OrderController.class, CacheConfig.class})
    @ComponentScan(basePackageClasses = {ProductMapper.class, OrderMapper.class})
    static class TestConfig {
        @Bean
        public ProductRepository productRepository() {
            return mock(ProductRepository.class);
        }
        
        @Bean
        public OrderRepository orderRepository() {
            return mock(OrderRepository.class);
        }

        @Bean
        public CustomerRepository customerRepository() {
            return mock(CustomerRepository.class);
        }

    }

    @Autowired
    private ProductController productController;

    @Autowired
    private OrderController orderController;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testGetProductReturnsOrderIds() {
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");
        
        Order order = new Order();
        order.setId(10L);
        order.setDescription("Test Order");
        
        product.setOrders(Set.of(order));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productController.getProductById(1L);
        
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getDescription());
        assertEquals(1, result.getOrderIds().size());
        assertEquals(10L, result.getOrderIds().get(0));
    }

    @Test
    void testGetOrderReturnsProducts() {
        Order order = new Order();
        order.setId(10L);
        order.setDescription("Test Order");
        
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");
        
        order.setProducts(Set.of(product));

        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

        OrderDTO result = orderController.getOrderById(10L);
        
        assertEquals(10L, result.getId());
        assertEquals(1, result.getProducts().size());
        assertEquals(1L, result.getProducts().get(0).getId());
        assertEquals("Test Product", result.getProducts().get(0).getDescription());
    }

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private org.springframework.cache.CacheManager cacheManager;

    @Test
    void testGetOrderReturnsCustomers() {
        if (cacheManager.getCache("orders") != null) {
            cacheManager.getCache("orders").clear();
        }

        Order order = new Order();
        order.setId(10L);
        order.setDescription("Test Order");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        order.setCustomers(new java.util.HashSet<>(java.util.Set.of(customer)));

        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

        OrderDTO result = orderController.getOrderById(10L);
        
        assertEquals(10L, result.getId());
        assertNotNull(result.getCustomers());
        assertEquals(1, result.getCustomers().size());
        assertEquals(1L, result.getCustomers().get(0).getId());
        assertEquals("John Doe", result.getCustomers().get(0).getName());
    }
}
