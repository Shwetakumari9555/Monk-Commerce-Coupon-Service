package com.monk.coupans.repository;

import com.monk.coupans.entity.BxGyCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BxGyRepository extends JpaRepository<BxGyCoupon, Long> {

    BxGyCoupon findByCouponId(Long couponId);

    boolean existsByRepetitionLimit(int repetitionLimit);
}