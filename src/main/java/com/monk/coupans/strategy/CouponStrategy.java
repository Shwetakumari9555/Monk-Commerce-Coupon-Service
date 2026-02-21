package com.monk.coupans.strategy;

import com.monk.coupans.dto.CartRequest;
import com.monk.coupans.entity.Coupon;

public interface CouponStrategy {
    boolean isApplicable(CartRequest cart, Coupon coupon);
    double calculateDiscount(CartRequest cart, Coupon coupon);
}
