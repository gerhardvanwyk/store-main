package com.example.store.config;

import com.example.store.controller.CustomerController;
import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ChronicleMapIntegrationTest.TestConfig.class, properties = {
    "app.cache.chronicle-map.file=test_customers_cache.dat",
    "app.cache.chronicle-map.entries=10"
})
public class ChronicleMapIntegrationTest {

    @Import({CacheConfig.class, CustomerController.class})
    @ComponentScan(basePackageClasses = CustomerMapper.class)
    static class TestConfig {
    }

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        if (cacheManager.getCache("customers") != null) {
            cacheManager.getCache("customers").clear();
        }
        // Attempt to delete test files
        new File("test_customers_cache.dat").delete();
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
    void testCreateCustomerEvictsCache() {
        Customer customer = new Customer();
        customer.setName("Evict User");
        customer.setId(2L);

        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Populate cache
        customerController.getAllCustomers();
        verify(customerRepository, times(1)).findAll();
        
        // Evict cache
        customerController.createCustomer(customer);
        
        // Should call repository again
        customerController.getAllCustomers();
        
        verify(customerRepository, times(2)).findAll();
    }
}
