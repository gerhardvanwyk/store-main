package com.example.store.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class OrderDTO implements Serializable {
    private Long id;
    private String description;
    private OrderCustomerDTO customer;
    private List<ProductDTO> products;
}
