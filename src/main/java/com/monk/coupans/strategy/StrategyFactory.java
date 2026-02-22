package com.monk.coupans.strategy;

import com.monk.coupans.entity.CouponType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/*
 * Factory class responsible for returning the correct
 * coupon strategy implementation based on coupon type.
 *
 * Helps follow Open-Closed Principle.
 */
@Component
@RequiredArgsConstructor
public class StrategyFactory {

    private final CartWiseStrategy cart;
    private final ProductWiseStrategy product;
    private final BxGyStrategy bxgy;

    public CouponStrategy getStrategy(CouponType type) {
        return switch (type) {
            case CART_WISE -> cart;
            case PRODUCT_WISE -> product;
            case BXGY -> bxgy;
        };
    }
}
