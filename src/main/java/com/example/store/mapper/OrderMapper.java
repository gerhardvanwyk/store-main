package com.example.store.mapper;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderMapper {

    @org.mapstruct.Mapping(source = "customers", target = "customers")
    OrderDTO orderToOrderDTO(Order order);

    List<OrderDTO> ordersToOrderDTOs(java.util.Collection<Order> orders);

    OrderCustomerDTO customerToOrderCustomerDTO(Customer customer);

    List<OrderCustomerDTO> customersToOrderCustomerDTOs(java.util.Collection<Customer> customers);
}
