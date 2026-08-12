package com.example.store.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class OrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String description;
    private OrderCustomerDTO customer;
    private List<ProductDTO> products;
}
