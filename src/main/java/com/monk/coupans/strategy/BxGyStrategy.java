package com.monk.coupans.strategy;

import com.monk.coupans.dto.CartItem;
import com.monk.coupans.dto.CartRequest;
import com.monk.coupans.entity.BxGyCoupon;
import com.monk.coupans.entity.Coupon;
import com.monk.coupans.repository.BxGyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BxGyStrategy implements CouponStrategy {

    private final BxGyRepository repo;

    @Override
    public boolean isApplicable(CartRequest cart, Coupon coupon) {
        return true;
    }

    @Override
    public double calculateDiscount(CartRequest cart, Coupon coupon) {
        BxGyCoupon bx = repo.findByCouponId(coupon.getId());
        int totalQty = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity).sum();
        int times = Math.min(totalQty / 2, bx.getRepetitionLimit());
        return times * 50;
    }
}