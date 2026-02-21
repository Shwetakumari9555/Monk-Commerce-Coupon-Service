package com.monk.coupans.repository;

import com.monk.coupans.entity.ProductCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCouponRepository extends JpaRepository<ProductCoupon, Long> {

    ProductCoupon findByCouponId(Long couponId);

    boolean existsByProductIdAndDiscount(Long productId, double discount);
}
