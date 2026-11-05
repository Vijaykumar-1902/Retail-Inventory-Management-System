package com.retail.inventory.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.retail.inventory.entity.Order;
import com.retail.inventory.exception.ResourceNotFoundException;
import com.retail.inventory.repository.CustomerRepository;
import com.retail.inventory.repository.OrderItemRepository;
import com.retail.inventory.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CustomerRepository customerRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
    }

    // Create Order
    public Order createOrder(Order order) {

        if (order.getCustomer() == null
                || order.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        order.setCustomer(customerRepository.findById(
                order.getCustomer().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found")));

        order.setTotalAmount(BigDecimal.ZERO);

        return orderRepository.save(order);
    }

    // Get All Orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get Order By ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + id));
    }

    // Update Order
    public Order updateOrder(Long id, Order updatedOrder) {

        Order existingOrder = getOrderById(id);

        existingOrder.setOrderDate(updatedOrder.getOrderDate());
        existingOrder.setStatus(updatedOrder.getStatus());

        if (updatedOrder.getCustomer() != null) {

            Long customerId = updatedOrder.getCustomer().getId();

            if (customerId == null) {
                throw new IllegalArgumentException(
                        "Customer ID is required");
            }

            existingOrder.setCustomer(
                    customerRepository.findById(customerId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Customer not found with ID: " + customerId)));
        }

        return orderRepository.save(existingOrder);
    }

    // Delete Order
    public void deleteOrder(Long id) {

        Order order = getOrderById(id);

        if (!orderItemRepository.findByOrderId(id).isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot delete order because it contains order items");
        }

        orderRepository.delete(order);
    }

    // Search Orders By Status
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatusIgnoreCase(status);
    }

    // Filter Orders By Date Range
    public List<Order> getOrdersByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        if (startDate == null || endDate == null
                || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }

    // Get Orders By Customer ID
    public List<Order> getOrdersByCustomer(Long customerId) {

        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer not found with ID: " + customerId);
        }

        return orderRepository.findByCustomerId(customerId);
    }

    // Calculate and Save Order Total
    @Transactional
    public BigDecimal calculateOrderTotal(Long orderId) {

        Order order = getOrderById(orderId);

        BigDecimal total =
                orderItemRepository.calculateOrderTotal(orderId);

        if (total == null) {
            total = BigDecimal.ZERO;
        }

        order.setTotalAmount(total);

        orderRepository.save(order);

        return total;
    }
}