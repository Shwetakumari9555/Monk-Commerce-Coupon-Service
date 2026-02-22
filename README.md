# Monk-Commerce-Coupon-Service
Production-ready Coupon Management API built with Spring Boot (Java 21).  Supports Cart-Wise, Product-Wise, and Buy-X-Get-Y coupons using Strategy Pattern for extensibility.  Designed to handle real-world coupon scenarios with clean architecture, SOLID principles, and scalable design.

# 🧾 Coupons Management API
### Monk Commerce Backend Assignment — Spring Boot (Java 21)

---

## 📌 Overview

This project implements a **Coupon Management System** that supports multiple discount types and applies them dynamically to a shopping cart.

The solution is designed with:

- Clean layered architecture
- Strategy design pattern
- Normalized database schema
- Concurrency-safe coupon usage
- Real-world business rule handling

---

## 🏗️ Architecture Diagram

![Architecture Diagram](docs/overview.png)

---

## 🔄 System Flow

Client (Postman / UI)  
↓  
Controller Layer  
↓  
Service Layer (Business Logic)  
↓  
Strategy Factory  
↓  
Coupon Strategy (Cart / Product / BXGY)  
↓  
Repository Layer (JPA)  
↓  
Database Tables

---

## 🛠️ Tech Stack

Java 21  
Spring Boot  
Spring Data JPA (Hibernate)  
MySQL / H2 Database  
Lombok  
JUnit + Mockito

---

## 🎯 Supported Coupon Types

### Cart-Wise Coupon
Applies discount to entire cart if total exceeds a threshold.

Example:  
Spend ₹100 → Get 10% off

---

### Product-Wise Coupon
Applies discount only to specific products.

Example:  
Product ID 5 → 20% discount

---

### Buy-X-Get-Y (BXGY) Coupon
Provides free items based on purchase quantity.

Example:  
Buy 3 items → Get 1 free

---

## ⭐ Default Business Rules

If not provided during creation:

Expiry Date → 1 month from creation date  
Usage Limit → 20 uses

Defaults are handled automatically using JPA lifecycle callback (`@PrePersist`).

---

### Duplicate Coupon Validation
- Prevents creation of duplicate coupons:
    - Same threshold & discount for cart-wise.
    - Same productId & discount for product-wise.
    - Same repetition limit for BXGY.
    - 
Ensures database integrity and avoids redundant rules.

---

## 🔐 Concurrency Handling

The system uses **Optimistic Locking (`@Version`)** to prevent:

- Multiple users applying the same coupon simultaneously
- Negative usage limits
- Race conditions during updates

---

## 🗄️ Database Design

Each coupon type has its own table:

coupon → Base coupon info  
cart_coupon → Cart-wise rules  
product_coupon → Product-wise rules  
bxgy_coupon → BXGY rules

This ensures:

- No NULL columns
- Clean separation of data
- Easy extensibility

---

## 🚀 API Endpoints

### Coupon CRUD APIs

POST /coupons → Create coupon  
GET /coupons → Get all coupons  
GET /coupons/{id} → Get coupon by ID  
PUT /coupons/{id} → Update coupon  
DELETE /coupons/{id} → Delete coupon

---

### Coupon Application APIs

POST /coupons/applicable-coupons → Get applicable coupons  
POST /coupons/apply-coupon/{id} → Apply coupon

---

## 🧪 Example Request Payloads

Create Cart-Wise Coupon:

{
"type": "CART_WISE",
"details": {
"threshold": 100,
"discount": 10
}
}

Create Product-Wise Coupon:

{
"type": "PRODUCT_WISE",
"details": {
"product_id": 1,
"discount": 20
}
}

Create BXGY Coupon:

{
"type": "BXGY",
"details": {
"repition_limit": 2
}
}

---

## ✅ Implemented Use Cases

Coupon creation for all types  
Default expiry and usage handling  
Duplicate rule prevention  
Fetching coupons with subtype details  
Discount calculation logic  
Expiry and usage validation  
Concurrency-safe coupon application  
Error handling

---

## ⚠️ Edge Cases Considered

Empty cart  
Invalid quantity or price  
Coupon applied to wrong product  
Cart below threshold  
Multiple applicable coupons  
Concurrent coupon usage

---

## 📌 Assumptions

Each coupon belongs to only one type  
Each coupon has exactly one subtype record  
Only one coupon applied at a time  
Product IDs in cart are valid

---

## ❗ Limitations

No coupon stacking logic  
No user-specific coupon rules  
No distributed caching  
Simplified BXGY calculation

---

## 🔮 Future Enhancements

Coupon stacking support  
Category-based coupons  
User-specific coupons  
Analytics dashboard  
Caching layer

---

## 🛠️ Database Setup (Simple Steps)

Step 1 — Install MySQL

Step 2 — Create database:

CREATE DATABASE coupon_db;

Step 3 — Configure application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/coupon_db  
spring.datasource.username=root  
spring.datasource.password=yourpassword  
spring.jpa.hibernate.ddl-auto=update

Step 4 — Run application:

mvn spring-boot:run

Tables will be created automatically.

---

## 👀 Monitoring Database Easily

You can use:

MySQL Workbench  
DBeaver  
phpMyAdmin

These tools allow you to view tables and monitor coupon usage.

---

## 🧪 How to Test APIs Using Postman

Start the application.

Base URL:

http://localhost:8080

---

## 🔧 cURL Commands for Testing APIs

### Create Cart-Wise Coupon

curl -X POST http://localhost:8080/coupons/create \
-H "Content-Type: application/json" \
-d '{
"type":"CART_WISE",
"details":{"threshold":100,"discount":10}
}'

---

### Create Product-Wise Coupon

curl -X POST http://localhost:8080/coupons/fetchAll \
-H "Content-Type: application/json" \
-d '{
"type":"PRODUCT_WISE",
"details":{"product_id":1,"discount":20}
}'

---

### Create BXGY Coupon

curl -X POST http://localhost:8080/coupons \
-H "Content-Type: application/json" \
-d '{
"type":"BXGY",
"details":{"repition_limit":2}
}'

---

### Get All Coupons

curl http://localhost:8080/coupons

---

### Get Coupon By ID

curl http://localhost:8080/coupons/1

---

### Delete Coupon

curl -X DELETE http://localhost:8080/coupons/1

---

### Get Applicable Coupons

curl -X POST http://localhost:8080/coupons/applicableCoupons \
-H "Content-Type: application/json" \
-d '{
"items":[{"productId":1,"quantity":3,"price":50}]
}'

---

### Apply Coupon

curl -X POST http://localhost:8080/coupons/applyCoupon/1 \
-H "Content-Type: application/json" \
-d '{
"items":[{"productId":1,"quantity":3,"price":50}]
}'

---

## 🧪 Testing Included

JUnit + Mockito unit tests  
Controller tests using Mockito  
Validation and concurrency tests

---

## ▶️ Running the Project

mvn clean install  
mvn spring-boot:run

Server runs at:

http://localhost:8080

---

## 🏁 Conclusion

This implementation delivers a robust, scalable, and production-ready coupon management system capable of handling multiple discount scenarios efficiently while ensuring data consistency and extensibility.