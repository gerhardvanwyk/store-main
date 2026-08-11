package com.example.store.repository;

import com.example.store.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


    @Query("""
            SELECT c FROM Customer c
            WHERE LOWER(c.name) = LOWER(:name)
               OR LOWER(c.name) LIKE LOWER(CONCAT(:name, ' %'))
               OR LOWER(c.name) LIKE LOWER(CONCAT('% ', :name))
               OR LOWER(c.name) LIKE LOWER(CONCAT('% ', :name, ' %'))
            """)
    Optional<Customer> findByName(@Param("name") String name);
}
