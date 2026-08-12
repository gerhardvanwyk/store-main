package com.example.store.repository;

import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private void clearDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        clearDatabase();
        Customer customer = new Customer();
        customer.setName("Jane Doe");
        
        Customer savedCustomer = customerRepository.save(customer);
        
        assertThat(savedCustomer.getId()).isNotNull();
        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getName()).isEqualTo("Jane Doe");
    }

    @Test
    void testFindByNameExact() {
        clearDatabase();
        Customer customer = new Customer();
        customer.setName("John Smith");
        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByName("John Smith");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John Smith");
    }

    @Test
    void testFindByNameCaseInsensitive() {
        clearDatabase();
        Customer customer = new Customer();
        customer.setName("John Smith");
        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByName("john smith");
        assertThat(found).isPresent();
    }

    @Test
    void testFindByNamePrefix() {
        clearDatabase();
        Customer customer = new Customer();
        customer.setName("John Smith");
        customerRepository.save(customer);

        // Matching "John %"
        Optional<Customer> found = customerRepository.findByName("John");
        assertThat(found).isPresent();
    }

    @Test
    void testFindByNameSuffix() {
        clearDatabase();
        Customer customer = new Customer();
        customer.setName("John Smith");
        customerRepository.save(customer);

        // Matching "% Smith"
        Optional<Customer> found = customerRepository.findByName("Smith");
        assertThat(found).isPresent();
    }

    @Test
    void testFindByNameMiddle() {
        clearDatabase();
        Customer customer = new Customer();
        customer.setName("John Alan Smith");
        customerRepository.save(customer);

        // Matching "% Alan %"
        Optional<Customer> found = customerRepository.findByName("Alan");
        assertThat(found).isPresent();
    }

    @Test
    void testFindByNameNotFound() {
        clearDatabase();
        Optional<Customer> found = customerRepository.findByName("Nonexistent");
        assertThat(found).isEmpty();
    }
}
