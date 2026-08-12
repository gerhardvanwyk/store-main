package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
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
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @GetMapping
    public Page<ProductDTO> getAllProducts(@PageableDefault(size = 20) Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::productToProductDTO);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(productMapper::productToProductDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "products", allEntries = true)
    public ProductDTO createProduct(@RequestBody Product product) {
        return productMapper.productToProductDTO(productRepository.save(product));
    }
}
