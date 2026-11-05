package com.retail.inventory.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.retail.inventory.entity.Order;
import com.retail.inventory.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create Order
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @Valid @RequestBody Order order) {

        return new ResponseEntity<>(
                orderService.createOrder(order),
                HttpStatus.CREATED);
    }

    // Get All Orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Get Order By ID
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Update Order
    @PutMapping("/{id}")
    public Order updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody Order order) {

        return orderService.updateOrder(id, order);
    }

    // Delete Order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {

        orderService.deleteOrder(id);

        return ResponseEntity.ok("Order deleted successfully");
    }

    // Search Orders By Status
    @GetMapping("/status")
    public List<Order> getOrdersByStatus(
            @RequestParam String status) {

        return orderService.getOrdersByStatus(status);
    }

    // Filter Orders By Date Range
    @GetMapping("/date-range")
    public List<Order> getOrdersByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate) {

        return orderService.getOrdersByDateRange(startDate, endDate);
    }

    // Get Orders By Customer ID
    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomer(
            @PathVariable Long customerId) {

        return orderService.getOrdersByCustomer(customerId);
    }

    // Calculate Order Total
    @GetMapping("/{id}/total")
    public BigDecimal calculateOrderTotal(@PathVariable Long id) {
        return orderService.calculateOrderTotal(id);
    }
}