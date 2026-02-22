package com.monk.coupans.strategy;

import com.monk.coupans.dto.CartRequest;
import com.monk.coupans.entity.Coupon;
/*
 * Defines contract for all coupon discount strategies.
 * Each coupon type implements its own calculation logic.
 */
public interface CouponStrategy {
    boolean isApplicable(CartRequest cart, Coupon coupon);
    double calculateDiscount(CartRequest cart, Coupon coupon);
}
