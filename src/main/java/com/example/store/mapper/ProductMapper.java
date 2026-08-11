package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "orderIds", expression = "java(mapOrdersToIds(product))")
    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productsToProductDTOs(List<Product> all);

    default List<Long> mapOrdersToIds(Product product) {
        if (product.getOrders() == null) {
            return null;
        }
        return product.getOrders().stream()
                .map(Order::getId)
                .collect(Collectors.toList());
    }
}
