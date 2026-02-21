package com.monk.coupans.strategy;

import com.monk.coupans.dto.CartRequest;
import com.monk.coupans.entity.CartCoupon;
import com.monk.coupans.entity.Coupon;
import com.monk.coupans.repository.CartCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartWiseStrategy implements CouponStrategy {

    private final CartCouponRepository repo;

    @Override
    public boolean isApplicable(CartRequest cart, Coupon coupon) {
        CartCoupon cc = repo.findByCouponId(coupon.getId());
        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        return total >= cc.getThreshold();
    }

    @Override
    public double calculateDiscount(CartRequest cart, Coupon coupon) {
        CartCoupon cc = repo.findByCouponId(coupon.getId());
        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        return total * cc.getDiscount() / 100;
    }

}
