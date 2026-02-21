package com.monk.coupans.exceptions;

public class InvalidCouponException extends RuntimeException {
    public InvalidCouponException(String msg) {
        super(msg);
    }
}