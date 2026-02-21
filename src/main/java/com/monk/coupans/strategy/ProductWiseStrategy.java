package com.monk.coupans.strategy;

import com.monk.coupans.dto.CartRequest;
import com.monk.coupans.entity.Coupon;
import com.monk.coupans.entity.ProductCoupon;
import com.monk.coupans.repository.ProductCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductWiseStrategy implements CouponStrategy {

    private final ProductCouponRepository repo;

    @Override
    public boolean isApplicable(CartRequest cart, Coupon coupon) {
        ProductCoupon pc = repo.findByCouponId(coupon.getId());
        return cart.getItems().stream()
                .anyMatch(i -> i.getProductId().equals(pc.getProductId()));
    }

    @Override
    public double calculateDiscount(CartRequest cart, Coupon coupon) {
        ProductCoupon pc = repo.findByCouponId(coupon.getId());
        return cart.getItems().stream()
                .filter(i -> i.getProductId().equals(pc.getProductId()))
                .mapToDouble(i -> i.getPrice() * i.getQuantity() * pc.getDiscount() / 100)
                .sum();
    }
}