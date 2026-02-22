package com.monk.coupans.repository;

import com.monk.coupans.entity.BxGyCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Repository interface for performing database operations
 * using Spring Data JPA.
 *
 * Provides CRUD operations and custom query methods.
 */
public interface BxGyRepository extends JpaRepository<BxGyCoupon, Long> {

    BxGyCoupon findByCouponId(Long couponId);

    boolean existsByRepetitionLimit(int repetitionLimit);
}