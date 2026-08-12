package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @GetMapping
    public Page<OrderDTO> getAllOrders(@PageableDefault(size = 20) Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::orderToOrderDTO);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "orders", key = "#id")
    public OrderDTO getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::orderToOrderDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "orders", allEntries = true)
    public OrderDTO createOrder(@RequestBody Order order) {
        Order saved = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(saved);
    }
}
