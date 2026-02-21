package com.monk.coupans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_coupon")
public class ProductCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private double discount;

    @OneToOne
    @JoinColumn(name = "coupon_id", unique = true)
    private Coupon coupon;
}
