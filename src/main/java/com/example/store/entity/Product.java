package com.example.store.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "\"product\"")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;
}
