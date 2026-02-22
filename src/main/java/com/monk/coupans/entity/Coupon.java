package com.monk.coupans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
/*
 * Base entity representing a coupon.
 * Stores common attributes shared across all coupon types.
 *
 * Each specific coupon type (Cart/Product/BXGY) has its own table
 * linked via a one-to-one relationship.
 *
 * Optimistic locking (@Version) is used to handle concurrency
 * when multiple users try to apply the same coupon simultaneously.
 */
@Entity
@Getter
@Setter
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Type of coupon (CART_WISE, PRODUCT_WISE, BXGY)
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CouponType type;

    // Expiry date after which coupon cannot be applied
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    // Number of times this coupon can be used
    @Column(name = "usage_limit")
    private Integer usageLimit;

    // Used for optimistic locking to prevent race conditions
    @Version
    private Long version;

    /*
     * Automatically sets default values before saving.
     * If expiry date is not provided → defaults to 1 month.
     * If usage limit is not provided → defaults to 20 uses.
     */
    @PrePersist
    public void setDefaults() {

        // Default expiry = 1 month from creation time
        if (this.expiryDate == null) {
            this.expiryDate = LocalDateTime.now().plusMonths(1);
        }

        // Default usage limit = 20 uses
        if (this.usageLimit == null) {
            this.usageLimit = 20;
        }
    }
}
