# Monk-Commerce-Coupon-Service
Production-ready Coupon Management API built with Spring Boot (Java 21).  Supports Cart-Wise, Product-Wise, and Buy-X-Get-Y coupons using Strategy Pattern for extensibility.  Designed to handle real-world coupon scenarios with clean architecture, SOLID principles, and scalable design.

# Coupons Management API — Monk Commerce Assignment

## Project Overview

This project implements a **Coupon Management System** using **Spring Boot and Java 21**.

The system supports multiple coupon types and applies discounts dynamically to a shopping cart.

It is designed with:

* Clean architecture
* Normalized database schema
* Strategy design pattern
* Concurrency handling
* Extensible coupon types

---

## Coupon Types Supported

### Cart-Wise Coupon

Applies discount if cart total exceeds threshold.

Example:

* Spend ₹100 → Get 10% discount

---

### Product-Wise Coupon

Applies discount only to specific products.

Example:

* Product ID = 5 → 20% discount

---

### Buy-X-Get-Y Coupon

Provides free items based on purchase quantity.

Example:

* Buy 3 → Get 1 free

---

## Default Business Rules

If not provided:

* Expiry Date = 1 month from creation
* Usage Limit = 20 uses

---

## System Flow Diagram

```
User Request
      ↓
Controller Layer
      ↓
Service Layer
      ↓
Strategy Factory
      ↓
Coupon Strategy (Cart/Product/BxGy)
      ↓
Repositories
      ↓
Database Tables
```

---

## Database Schema Design

Tables:

* coupon
* cart_coupon
* product_coupon
* bxgy_coupon

This avoids NULL columns and ensures clean separation.

---

## Setting Up Database (Simple Steps)

1. Install MySQL
2. Create a database:

```
CREATE DATABASE coupon_db;
```

3. Update application.properties:

```
spring.datasource.url=jdbc:mysql://localhost:3306/coupon_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

4. Run the application — tables will be created automatically.

---

## Monitoring Database Easily

You can use:

* MySQL Workbench
* DBeaver
* phpMyAdmin

These tools allow you to:

* View tables
* Check stored coupons
* Monitor usage limits
* Verify discount data

---

## How to Test APIs Using Postman

1. Start Spring Boot application
2. Open Postman
3. Use base URL:

```
http://localhost:8080
```

4. Test endpoints:

* POST /coupons
* GET /coupons
* POST /coupons/apply-coupon/{id}

Import cURL commands into Postman for quick testing.

---

## Error Handling Covered

* Invalid coupon type
* Coupon not found
* Coupon expired
* Usage limit exceeded
* Invalid cart input

---

## Concurrency Handling

Optimistic locking using `@Version` ensures:

* No duplicate usage
* Safe updates
* Race condition prevention

---

## Unit Testing

JUnit + Mockito tests cover:

* Coupon creation
* Discount application
* Expiry validation
* Usage limit checks

---

## How to Run the Project

```
mvn spring-boot:run
```

Server starts at:

```
http://localhost:8080
```

---

## Future Enhancements

* Coupon stacking
* Category based coupons
* User specific coupons
* Real-time analytics

---

## Conclusion

This system provides a scalable and production-ready coupon engine capable of handling diverse discount scenarios efficiently.
