package com.retail.inventory.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.retail.inventory.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItem oi WHERE oi.order.id = :orderId")
    BigDecimal calculateOrderTotal(@Param("orderId") Long orderId);
}