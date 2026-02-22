package com.monk.coupans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/*
 * Stores configuration for Cart-Wise coupons.
 * Contains threshold amount and discount percentage.
 * Linked to base Coupon entity via one-to-one mapping.
 */
@Entity
@Getter
@Setter
@Table(name = "cart_coupon")
public class CartCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Minimum cart total required to apply coupon
    @Column(nullable = false)
    private double threshold;

    // Discount percentage applied on total cart value
    @Column(nullable = false)
    private double discount;

    @OneToOne
    @JoinColumn(name = "coupon_id", unique = true)
    private Coupon coupon;
}
