package com.monk.coupans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CouponType type;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "usage_limit")
    private Integer usageLimit;

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
