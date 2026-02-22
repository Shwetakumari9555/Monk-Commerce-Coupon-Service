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

    /**
     * Check if the product required by coupon exists in cart.
     */
    @Override
    public boolean isApplicable(CartRequest cart, Coupon coupon) {

        ProductCoupon pc = repo.findByCouponId(coupon.getId());

        if (pc == null) return false;

        return cart.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(pc.getProductId()));
    }

    /**
     * Calculate discount ONLY for that product.
     * Not on entire cart.
     */
    @Override
    public double calculateDiscount(CartRequest cart, Coupon coupon) {

        ProductCoupon pc = repo.findByCouponId(coupon.getId());

        if (pc == null) return 0;

        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(pc.getProductId()))
                .mapToDouble(item ->
                        item.getPrice() * item.getQuantity() * pc.getDiscount() / 100
                )
                .sum();
    }
}