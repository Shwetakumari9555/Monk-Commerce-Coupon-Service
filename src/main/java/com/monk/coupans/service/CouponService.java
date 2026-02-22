package com.monk.coupans.service;

import com.monk.coupans.dto.*;
import com.monk.coupans.wrapperDto.ApplicableCouponResponseWrapper;
import com.monk.coupans.wrapperDto.CartRequestWrapper;
import com.monk.coupans.wrapperDto.CartResponseWrapper;

import java.util.List;

public interface CouponService {

    CouponResponse createCouponFromRequest(CouponCreateRequest request);

    List<CouponResponse> getAllCoupons();

    CouponResponse getCoupon(Long id);

    CouponResponse updateCoupon(Long id, CouponCreateRequest coupon);

    void deleteCoupon(Long id);

    ApplicableCouponResponseWrapper applicableCoupons(CartRequestWrapper cart);

    CartResponseWrapper applyCoupon(Long id, CartRequestWrapper cart);
}
