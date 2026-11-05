package com.retail.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.retail.inventory.entity.Order;
import com.retail.inventory.entity.OrderItem;
import com.retail.inventory.entity.Product;
import com.retail.inventory.exception.ResourceNotFoundException;
import com.retail.inventory.repository.OrderItemRepository;
import com.retail.inventory.repository.OrderRepository;
import com.retail.inventory.repository.ProductRepository;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public OrderItemService(
            OrderItemRepository orderItemRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository,
            OrderService orderService) {

        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    // Add Order Item and Reduce Stock
    @Transactional
    public OrderItem addOrderItem(OrderItem orderItem) {

        if (orderItem.getOrder() == null
                || orderItem.getOrder().getId() == null) {
            throw new IllegalArgumentException("Order ID is required");
        }

        if (orderItem.getProduct() == null
                || orderItem.getProduct().getId() == null) {
            throw new IllegalArgumentException("Product ID is required");
        }

        if (orderItem.getQuantity() == null
                || orderItem.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        Order order = orderRepository.findById(
                orderItem.getOrder().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found"));

        Product product = productRepository.findById(
                orderItem.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found"));

        if (product.getQuantity() < orderItem.getQuantity()) {
            throw new IllegalArgumentException(
                    "Insufficient stock available");
        }

        product.setQuantity(
                product.getQuantity() - orderItem.getQuantity());

        productRepository.save(product);

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPrice(product.getPrice());

        OrderItem savedItem = orderItemRepository.save(orderItem);

        orderService.calculateOrderTotal(order.getId());

        return savedItem;
    }

    // Get All Order Items
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    // Get Order Item By ID
    public OrderItem getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order item not found with ID: " + id));
    }

    // Update Order Item and Adjust Stock
    @Transactional
    public OrderItem updateOrderItem(Long id, OrderItem updatedItem) {

        OrderItem existingItem = getOrderItemById(id);

        if (updatedItem.getOrder() == null
                || updatedItem.getOrder().getId() == null) {
            throw new IllegalArgumentException("Order ID is required");
        }

        if (updatedItem.getProduct() == null
                || updatedItem.getProduct().getId() == null) {
            throw new IllegalArgumentException("Product ID is required");
        }

        Integer newQuantity = updatedItem.getQuantity();

        if (newQuantity == null || newQuantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        Long oldOrderId = existingItem.getOrder().getId();

        Order newOrder = orderRepository.findById(
                updatedItem.getOrder().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found"));

        Product oldProduct = existingItem.getProduct();

        Product newProduct = productRepository.findById(
                updatedItem.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found"));

        // Calculate available stock after restoring old quantity
        int availableStock = newProduct.getQuantity();

        if (newProduct.getId().equals(oldProduct.getId())) {
            availableStock += existingItem.getQuantity();
        }

        if (availableStock < newQuantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock available");
        }

        // Restore previous product stock
        oldProduct.setQuantity(
                oldProduct.getQuantity() + existingItem.getQuantity());

        // Reduce stock from the new product
        newProduct.setQuantity(
                newProduct.getQuantity() - newQuantity);

        productRepository.save(oldProduct);
        productRepository.save(newProduct);

        // Update order item
        existingItem.setOrder(newOrder);
        existingItem.setProduct(newProduct);
        existingItem.setQuantity(newQuantity);
        existingItem.setPrice(newProduct.getPrice());

        OrderItem savedItem = orderItemRepository.save(existingItem);

        // Recalculate old and new order totals
        orderService.calculateOrderTotal(oldOrderId);

        if (!oldOrderId.equals(newOrder.getId())) {
            orderService.calculateOrderTotal(newOrder.getId());
        }

        return savedItem;
    }

    // Delete Order Item and Restore Stock
    @Transactional
    public void deleteOrderItem(Long id) {

        OrderItem item = getOrderItemById(id);

        Product product = item.getProduct();

        product.setQuantity(
                product.getQuantity() + item.getQuantity());

        productRepository.save(product);

        Long orderId = item.getOrder().getId();

        orderItemRepository.delete(item);

        orderService.calculateOrderTotal(orderId);
    }
}