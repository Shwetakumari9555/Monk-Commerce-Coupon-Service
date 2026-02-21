package com.monk.coupans.exceptions;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(Long id) {
        super("Coupon not found with id: " + id);
    }
}
