package com.monk.coupans.strategy;

import com.monk.coupans.dto.CartRequest;
import com.monk.coupans.entity.CartCoupon;
import com.monk.coupans.entity.Coupon;
import com.monk.coupans.repository.CartCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CartWiseStrategy implements CouponStrategy {

    private final CartCouponRepository repo;

    private double calculateCartTotal(CartRequest cart) {
        return cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    @Override
    public boolean isApplicable(CartRequest cart, Coupon coupon) {

        if (coupon.getExpiryDate().isBefore(LocalDateTime.now()))
            return false;

        CartCoupon cc = repo.findByCouponId(coupon.getId());
        if (cc == null) return false;

        double total = calculateCartTotal(cart);
        return total >= cc.getThreshold();
    }

    @Override
    public double calculateDiscount(CartRequest cart, Coupon coupon) {

        CartCoupon cc = repo.findByCouponId(coupon.getId());
        if (cc == null) return 0;

        double total = calculateCartTotal(cart);
        return total * cc.getDiscount() / 100;
    }
}
