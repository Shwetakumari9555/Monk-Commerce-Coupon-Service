package com.monk.coupans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/*
 * Stores configuration for Buy-X-Get-Y coupons.
 * Defines how many times the offer can repeat.
 */
@Entity
@Getter
@Setter
@Table(name = "bxgy_coupon")
public class BxGyCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Maximum number of times free items can be granted
    @Column(nullable = false)
    private int repetitionLimit;

    @OneToOne
    @JoinColumn(name = "coupon_id", unique = true)
    private Coupon coupon;
}
