package com.retail.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.retail.inventory.entity.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Search customers by name (case-insensitive)
    List<Customer> findByNameContainingIgnoreCase(String name);

    // Search customer by email
    List<Customer> findByEmailContainingIgnoreCase(String email);
}