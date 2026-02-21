package com.monk.coupans.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "bxgy_coupon")
public class BxGyCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int repetitionLimit;

    @OneToOne
    @JoinColumn(name = "coupon_id", unique = true)
    private Coupon coupon;
}
