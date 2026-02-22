package com.monk.coupans.repository;

import com.monk.coupans.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Repository interface for performing database operations
 * using Spring Data JPA.
 *
 * Provides CRUD operations and custom query methods.
 */
public interface CouponRepository extends JpaRepository<Coupon, Long> {}