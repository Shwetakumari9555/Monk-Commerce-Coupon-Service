package com.monk.coupans.serviceImpl;

import com.monk.coupans.dto.*;
import com.monk.coupans.entity.*;
import com.monk.coupans.exceptions.CouponNotFoundException;
import com.monk.coupans.exceptions.InvalidCouponException;
import com.monk.coupans.repository.BxGyRepository;
import com.monk.coupans.repository.CartCouponRepository;
import com.monk.coupans.repository.CouponRepository;
import com.monk.coupans.repository.ProductCouponRepository;
import com.monk.coupans.service.CouponService;
import com.monk.coupans.strategy.CouponStrategy;
import com.monk.coupans.strategy.StrategyFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepo;
    private final CartCouponRepository cartRepo;
    private final ProductCouponRepository productRepo;
    private final BxGyRepository bxgyRepo;
    private final StrategyFactory factory;

    @Override
    public Coupon createCouponFromRequest(CouponCreateRequest request) {

        CouponType type = CouponType.valueOf(request.getType().toUpperCase());

        Coupon coupon = new Coupon();
        coupon.setType(type);
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon = couponRepo.save(coupon);

        Map<String, Object> details = request.getDetails();

        switch (type) {

            case CART_WISE -> {
                CartCoupon cc = new CartCoupon();
                cc.setCoupon(coupon);
                cc.setThreshold(Double.parseDouble(details.get("threshold").toString()));
                cc.setDiscount(Double.parseDouble(details.get("discount").toString()));
                cartRepo.save(cc);
            }

            case PRODUCT_WISE -> {
                ProductCoupon pc = new ProductCoupon();
                pc.setCoupon(coupon);
                pc.setProductId(Long.parseLong(details.get("product_id").toString()));
                pc.setDiscount(Double.parseDouble(details.get("discount").toString()));
                productRepo.save(pc);
            }

            case BXGY -> {
                BxGyCoupon bx = new BxGyCoupon();
                bx.setCoupon(coupon);
                bx.setRepetitionLimit(Integer.parseInt(details.get("repition_limit").toString()));
                bxgyRepo.save(bx);
            }
        }

        return coupon;
    }

    @Override
    public List<CouponResponse> getAllCoupons() {

        List<CouponResponse> list = new ArrayList<>();

        for (Coupon coupon : couponRepo.findAll()) {

            CouponResponse dto = new CouponResponse();
            dto.setId(coupon.getId());
            dto.setType(coupon.getType().name());
            dto.setExpiryDate(coupon.getExpiryDate());


            switch (coupon.getType()) {

                case CART_WISE -> {
                    CartCoupon cc = cartRepo.findByCouponId(coupon.getId());
                    dto.setThreshold(cc.getThreshold());
                    dto.setDiscount(cc.getDiscount());
                }

                case PRODUCT_WISE -> {
                    ProductCoupon pc = productRepo.findByCouponId(coupon.getId());
                    dto.setProductId(pc.getProductId());
                    dto.setDiscount(pc.getDiscount());
                }

                case BXGY -> {
                    BxGyCoupon bx = bxgyRepo.findByCouponId(coupon.getId());
                    dto.setRepetitionLimit(bx.getRepetitionLimit());
                }
            }

            list.add(dto);
        }

        return list;
    }

    @Override
    public Coupon getCoupon(Long id) {
        return couponRepo.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));
    }

    @Override
    public Coupon updateCoupon(Long id, Coupon coupon) {
        Coupon existing = getCoupon(id);
        existing.setExpiryDate(coupon.getExpiryDate());
        return couponRepo.save(existing);
    }

    @Override
    public void deleteCoupon(Long id) {
        couponRepo.deleteById(id);
    }

    @Override
    public List<DiscountResponse> applicableCoupons(CartRequest cart) {
        List<DiscountResponse> result = new ArrayList<>();

        for (Coupon coupon : couponRepo.findAll()) {
            CouponStrategy strategy = factory.getStrategy(coupon.getType());
            if (strategy.isApplicable(cart, coupon)) {
                double discount = strategy.calculateDiscount(cart, coupon);
                result.add(new DiscountResponse(coupon.getId(), coupon.getType(), discount));
            }
        }

        return result;
    }

    @Transactional
    @Override
    public CartResponse applyCoupon(Long id, CartRequest cart) {

        Coupon coupon = couponRepo.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        if (coupon.getExpiryDate()!=null &&
                coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidCouponException("Coupon expired");
        }

        if (coupon.getUsageLimit()!=null && coupon.getUsageLimit() <= 0) {
            throw new InvalidCouponException("Usage limit exceeded");
        }

        CouponStrategy strategy = factory.getStrategy(coupon.getType());

        if (!strategy.isApplicable(cart, coupon)) {
            throw new InvalidCouponException("Coupon not applicable");
        }

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice()*i.getQuantity())
                .sum();

        double discount = strategy.calculateDiscount(cart,coupon);

        if (coupon.getUsageLimit()!=null) {
            coupon.setUsageLimit(coupon.getUsageLimit() - 1);
            couponRepo.save(coupon);
        }

        return new CartResponse(total, discount, total-discount);
    }
}