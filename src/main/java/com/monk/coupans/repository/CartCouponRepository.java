package com.monk.coupans.repository;

import com.monk.coupans.entity.CartCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Repository interface for performing database operations
 * using Spring Data JPA.
 *
 * Provides CRUD operations and custom query methods.
 */
public interface CartCouponRepository extends JpaRepository<CartCoupon, Long> {

    CartCoupon findByCouponId(Long couponId);

    boolean existsByThresholdAndDiscount(double threshold, double discount);
}
