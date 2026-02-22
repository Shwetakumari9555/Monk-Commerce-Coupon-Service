package com.monk.coupans.repository;

import com.monk.coupans.entity.BxGyCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 * Repository interface for performing database operations
 * using Spring Data JPA.
 *
 * Provides CRUD operations and custom query methods.
 */
public interface BxGyRepository extends JpaRepository<BxGyCoupon, Long> {

    BxGyCoupon findByCouponId(Long couponId);


    @Query("""
        SELECT DISTINCT c
        FROM BxGyCoupon c
        LEFT JOIN FETCH c.buyProducts
        LEFT JOIN FETCH c.getProducts
        WHERE c.repetitionLimit = :limit
    """)
    List<BxGyCoupon> findByLimitWithProducts(@Param("limit") int limit);
}