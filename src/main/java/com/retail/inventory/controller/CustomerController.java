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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.inventory.entity.Customer;
import com.retail.inventory.entity.Order;
import com.retail.inventory.service.CustomerService;
import com.retail.inventory.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;

    public CustomerController(
            CustomerService customerService,
            OrderService orderService) {

        this.customerService = customerService;
        this.orderService = orderService;
    }

    // Add Customer
    @PostMapping
    public ResponseEntity<Customer> addCustomer(
            @Valid @RequestBody Customer customer) {

        return new ResponseEntity<>(
                customerService.addCustomer(customer),
                HttpStatus.CREATED);
    }

    // Get All Customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Get Customer By ID
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    // Update Customer
    @PutMapping("/{id}")
    public Customer updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody Customer customer) {

        return customerService.updateCustomer(id, customer);
    }

    // Delete Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }

    // Search Customers By Name
    @GetMapping("/search")
    public List<Customer> searchCustomers(@RequestParam String name) {
        return customerService.searchCustomersByName(name);
    }

    // Get Orders By Customer ID
    @GetMapping("/{id}/orders")
    public List<Order> getCustomerOrders(@PathVariable Long id) {

        customerService.getCustomerById(id);

        return orderService.getOrdersByCustomer(id);
    }
}