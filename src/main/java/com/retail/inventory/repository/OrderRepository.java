package com.retail.inventory.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.retail.inventory.entity.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {

    // Search orders by status
    List<Order> findByStatusIgnoreCase(String status);

    // Filter orders by date range
    List<Order> findByOrderDateBetween(
            LocalDateTime startDate,
            LocalDateTime endDate);

    // Get orders by customer ID
    List<Order> findByCustomerId(Long customerId);
}