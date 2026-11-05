package com.retail.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.inventory.entity.OrderItem;
import com.retail.inventory.service.OrderItemService;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // Add Order Item
    @PostMapping
    public ResponseEntity<OrderItem> addOrderItem(
            @RequestBody OrderItem orderItem) {

        return new ResponseEntity<>(
                orderItemService.addOrderItem(orderItem),
                HttpStatus.CREATED);
    }

    // Get All Order Items
    @GetMapping
    public List<OrderItem> getAllOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    // Get Order Item By ID
    @GetMapping("/{id}")
    public OrderItem getOrderItemById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id);
    }

    // Update Order Item
    @PutMapping("/{id}")
    public OrderItem updateOrderItem(
            @PathVariable Long id,
            @RequestBody OrderItem orderItem) {

        return orderItemService.updateOrderItem(id, orderItem);
    }

    // Delete Order Item
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable Long id) {

        orderItemService.deleteOrderItem(id);

        return ResponseEntity.ok(
                "Order item deleted successfully and stock restored");
    }
}