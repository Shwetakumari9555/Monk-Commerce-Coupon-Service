package com.monk.coupans.service;

import com.monk.coupans.dto.*;
import com.monk.coupans.entity.Coupon;

import java.util.List;

public interface CouponService {

    Coupon createCouponFromRequest(CouponCreateRequest request);

    List<CouponResponse> getAllCoupons();

    CouponResponse getCoupon(Long id);

    CouponResponse updateCoupon(Long id, CouponCreateRequest coupon);

    void deleteCoupon(Long id);

    List<DiscountResponse> applicableCoupons(CartRequest cart);

    CartResponse applyCoupon(Long id, CartRequest cart);
}
