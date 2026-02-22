package com.monk.coupans.repository;

import com.monk.coupans.entity.ProductCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Repository interface for performing database operations
 * using Spring Data JPA.
 *
 * Provides CRUD operations and custom query methods.
 */
public interface ProductCouponRepository extends JpaRepository<ProductCoupon, Long> {

    ProductCoupon findByCouponId(Long couponId);

    boolean existsByProductIdAndDiscount(Long productId, double discount);
}
