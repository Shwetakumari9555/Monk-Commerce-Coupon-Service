package com.monk.coupans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.monk.coupans.entity.CouponType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountResponse {
    private Long couponId;
    private CouponType type;
    private double discount;
}
