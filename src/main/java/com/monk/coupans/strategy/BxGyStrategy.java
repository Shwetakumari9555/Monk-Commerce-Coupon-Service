package com.monk.coupans.strategy;

import com.monk.coupans.dto.CartItem;
import com.monk.coupans.dto.CartRequest;
import com.monk.coupans.entity.BxGyBuyProduct;
import com.monk.coupans.entity.BxGyCoupon;
import com.monk.coupans.entity.BxGyGetProduct;
import com.monk.coupans.entity.Coupon;
import com.monk.coupans.repository.BxGyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BxGyStrategy implements CouponStrategy {

    private final BxGyRepository repo;

    @Override
    public boolean isApplicable(CartRequest cart, Coupon coupon) {

        BxGyCoupon bx = repo.findByCouponId(coupon.getId());

        if (bx == null) return false;

        Set<BxGyBuyProduct> buyList = bx.getBuyProducts();
        if(buyList == null || buyList.isEmpty()) return false;

        int totalBuyQty = 0;

        for (CartItem item : cart.getItems()) {
            for (BxGyBuyProduct buy : bx.getBuyProducts()) {
                if (buy.getProductId().equals(item.getProductId())) {
                    totalBuyQty += item.getQuantity();
                }
            }
        }

        int requiredBuyQty = bx.getBuyProducts()
                .stream()
                .mapToInt(BxGyBuyProduct::getQuantity)
                .sum();

        if(requiredBuyQty == 0) return false;

        return totalBuyQty >= requiredBuyQty;
    }

    @Override
    public double calculateDiscount(CartRequest cart, Coupon coupon) {

        BxGyCoupon bx = repo.findByCouponId(coupon.getId());

        int totalBuyQty = 0;
        int totalGetQty = 0;
        Set<BxGyBuyProduct> buyList = bx.getBuyProducts();
        Set<BxGyGetProduct> getList = bx.getGetProducts();
        if(buyList == null || buyList.isEmpty() || getList == null || getList.isEmpty()) return 0;

        for (CartItem item : cart.getItems()) {

            for (BxGyBuyProduct buy : bx.getBuyProducts()) {
                if (buy.getProductId().equals(item.getProductId())) {
                    totalBuyQty += item.getQuantity();
                }
            }

            for (BxGyGetProduct get : bx.getGetProducts()) {
                if (get.getProductId().equals(item.getProductId())) {
                    totalGetQty += item.getQuantity();
                }
            }
        }

        int requiredBuyQty = bx.getBuyProducts()
                .stream()
                .mapToInt(BxGyBuyProduct::getQuantity)
                .sum();

        int requiredGetQty = bx.getGetProducts()
                .stream()
                .mapToInt(BxGyGetProduct::getQuantity)
                .sum();

        int times = Math.min(
                Math.min(totalBuyQty / requiredBuyQty,
                        totalGetQty / requiredGetQty),
                bx.getRepetitionLimit()
        );

        double discount = 0;

        for (CartItem item : cart.getItems()) {
            for (BxGyGetProduct get : bx.getGetProducts()) {

                if (get.getProductId().equals(item.getProductId())) {

                    int freeQty = times * get.getQuantity();
                    discount += freeQty * item.getPrice();
                }
            }
        }

        return discount;
    }
}