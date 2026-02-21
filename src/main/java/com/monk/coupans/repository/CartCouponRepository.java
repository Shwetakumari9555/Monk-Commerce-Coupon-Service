package com.monk.coupans.repository;

import com.monk.coupans.entity.CartCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartCouponRepository extends JpaRepository<CartCoupon, Long> {

    CartCoupon findByCouponId(Long couponId);

    boolean existsByThresholdAndDiscount(double threshold, double discount);
}
