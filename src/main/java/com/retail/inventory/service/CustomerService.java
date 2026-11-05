package com.retail.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.retail.inventory.entity.Customer;
import com.retail.inventory.exception.ResourceNotFoundException;
import com.retail.inventory.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Add Customer
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Get All Customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Get Customer By ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + id));
    }

    // Update Customer
    public Customer updateCustomer(Long id, Customer updatedCustomer) {

        Customer existingCustomer = getCustomerById(id);

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setAddress(updatedCustomer.getAddress());

        return customerRepository.save(existingCustomer);
    }

    // Delete Customer
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    // Search Customers By Name
    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    // Search Customers By Email
    public List<Customer> searchCustomersByEmail(String email) {
        return customerRepository.findByEmailContainingIgnoreCase(email);
    }
}