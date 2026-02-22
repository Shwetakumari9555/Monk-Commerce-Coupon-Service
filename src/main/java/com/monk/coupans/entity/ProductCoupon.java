package com.monk.coupans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/*
 * Stores configuration for Product-Wise coupons.
 * Applies discount only to specific products.
 */
@Entity
@Getter
@Setter
@Table(name = "product_coupon")
public class ProductCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Product ID eligible for discount
    @Column(nullable = false)
    private Long productId;

    // Discount percentage applied to the product
    @Column(nullable = false)
    private double discount;

    @OneToOne
    @JoinColumn(name = "coupon_id", unique = true)
    private Coupon coupon;
}
