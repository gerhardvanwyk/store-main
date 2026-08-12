package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO customerToCustomerDTO(Customer customer);

    List<CustomerDTO> customersToCustomerDTOs(java.util.Collection<Customer> customer);

    com.example.store.dto.CustomerOrderDTO orderToCustomerOrderDTO(com.example.store.entity.Order order);

    List<com.example.store.dto.CustomerOrderDTO> ordersToCustomerOrderDTOs(java.util.Collection<com.example.store.entity.Order> orders);
}
