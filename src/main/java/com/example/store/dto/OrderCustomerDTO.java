package com.example.store.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderCustomerDTO implements Serializable {
    private Long id;
    private String name;
}
