package com.example.store.config;

import com.example.store.controller.CustomerController;
import com.example.store.controller.OrderController;
import com.example.store.controller.ProductController;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.mapper.CustomerMapper;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ChronicleMapIntegrationTest.TestConfig.class, properties = {
    "app.cache.chronicle-map.customers=test_customers_cache.dat",
    "app.cache.chronicle-map.products=test_products_cache.dat",
    "app.cache.chronicle-map.orders=test_orders_cache.dat",
    "app.cache.chronicle-map.entries=10"
})
public class ChronicleMapIntegrationTest {

    @Import({CacheConfig.class, CustomerController.class, ProductController.class, OrderController.class})
    @ComponentScan(basePackageClasses = {CustomerMapper.class, OrderMapper.class})
    static class TestConfig {
    }

    @Autowired
    private CustomerController customerController;

    @Autowired
    private ProductController productController;

    @Autowired
    private OrderController orderController;

    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private OrderRepository orderRepository;

    @AfterEach
    void tearDown() {
        if (cacheManager.getCache("customers") != null) {
            cacheManager.getCache("customers").clear();
        }
        if (cacheManager.getCache("products") != null) {
            cacheManager.getCache("products").clear();
        }
        if (cacheManager.getCache("orders") != null) {
            cacheManager.getCache("orders").clear();
        }
    }

    @Test
    void testChronicleMapCacheIsUsed() {
        assertNotNull(cacheManager.getCache("customers"));
        
        Customer customer = new Customer();
        customer.setName("Test User");
        customer.setId(1L);

        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerRepository.findByName("Test User")).thenReturn(java.util.Optional.of(customer));

        // First call - repository should be called
        customerController.getAllCustomers();
        // Second call - should be cached
        customerController.getAllCustomers();

        verify(customerRepository, times(1)).findAll();

        // Test getCustomerByName
        customerController.getCustomerByName("Test User");
        customerController.getCustomerByName("Test User");
        verify(customerRepository, times(1)).findByName("Test User");
    }

    @Test
    void testProductCacheIsUsed() {
        assertNotNull(cacheManager.getCache("products"));

        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Test getAllProducts
        productController.getAllProducts();
        productController.getAllProducts();
        verify(productRepository, times(1)).findAll();

        // Test getProductById
        productController.getProductById(1L);
        productController.getProductById(1L);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateProductEvictsCache() {
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Evict Product");

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productController.getAllProducts();
        verify(productRepository, times(1)).findAll();

        productController.createProduct(product);

        productController.getAllProducts();
        verify(productRepository, times(2)).findAll();
    }

    @Test
    void testOrderCacheIsUsed() {
        assertNotNull(cacheManager.getCache("orders"));

        Order order = new Order();
        order.setId(1L);
        order.setDescription("Test Order");

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Test getAllOrders
        orderController.getAllOrders();
        orderController.getAllOrders();
        verify(orderRepository, times(1)).findAll();

        // Test getOrderById
        orderController.getOrderById(1L);
        orderController.getOrderById(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateOrderEvictsCache() {
        Order order = new Order();
        order.setId(1L);
        order.setDescription("Evict Order");

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        orderController.getAllOrders();
        verify(orderRepository, times(1)).findAll();

        orderController.createOrder(order);

        orderController.getAllOrders();
        verify(orderRepository, times(2)).findAll();
    }
}
