package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @GetMapping
    @Cacheable(value = "customers", key = "'all'")
    public List<CustomerDTO> getAllCustomers() {
        return customerMapper.customersToCustomerDTOs(customerRepository.findAll());
    }

    @GetMapping("/{name}")
    @Cacheable(value = "customers", key = "#id")
    public CustomerDTO getCustomerByName(@PathVariable String name) {
        return customerMapper.customerToCustomerDTO(customerRepository.findByName(name).orElseThrow());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "customers", allEntries = true)
    public CustomerDTO createCustomer(@RequestBody Customer customer) {
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }
}
