# Retail Inventory and Order Management System

## Project Overview
The Retail Inventory and Order Management System is a Java-based backend application developed using Spring Boot and MySQL. It provides REST APIs to manage products, categories, customers, orders, and order items.

The application helps maintain product stock, manage customer orders, calculate order totals, and perform inventory operations efficiently.

## Technologies Used
- Java 21
- Spring Boot 4.1.2
- Spring Data JPA
- Hibernate
- MySQL 8.0
- Maven
- REST APIs
- Postman
- Git and GitHub

## Features
- Product Management – Add, view, update, delete, search, and filter products.
- Category Management – Manage product categories.
- Customer Management – Create and manage customer details.
- Order Management – Create, update, and track customer orders.
- Order Item Management – Manage items associated with orders.
- Stock Management – Increase and decrease product stock automatically.
- Order Total Calculation – Calculate order totals based on order items.
- Input Validation – Validate user inputs.
- Exception Handling – Handle application errors using a global exception handler.
- Pagination and Sorting – Retrieve product records efficiently.

## Project Structure
- controller – REST API endpoints
- service – Business logic
- repository – Database operations
- entity – JPA entity classes
- dto – Data Transfer Objects
- exception – Custom exceptions and global exception handling

## Database Configuration
This project uses MySQL.

Create a database named `retail_inventory_db`.

Configure the following environment variable:

`DB_PASSWORD` – Your MySQL password.

The database connection settings are maintained in `application.properties`.

## How to Run the Project

1. Clone the repository.
2. Open the project in Spring Tool Suite or Eclipse.
3. Configure the MySQL database.
4. Set the `DB_PASSWORD` environment variable.
5. Run `RetailInventorySystemApplication.java`.

The application runs at:

`http://localhost:8080`

## API Testing
REST APIs were tested using Postman.

## Author
Vijay Kumar Reddy Mallangi

## GitHub Repository
https://github.com/Vijaykumar-1902/Retail-Inventory-Management-System