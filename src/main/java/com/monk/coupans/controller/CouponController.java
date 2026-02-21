package com.monk.coupans.controller;

import com.monk.coupans.dto.*;
import com.monk.coupans.entity.Coupon;
import com.monk.coupans.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService service;

    @PostMapping("/create")
    public Coupon create(@RequestBody CouponCreateRequest request) {
        return service.createCouponFromRequest(request);
    }

    @GetMapping("/fetch-all")
    public List<CouponResponse> getAll() {
        return service.getAllCoupons();
    }

    @GetMapping("/{id}")
    public Coupon get(@PathVariable Long id) {
        return service.getCoupon(id);
    }

    @PutMapping("/{id}")
    public Coupon update(@PathVariable Long id, @RequestBody Coupon coupon) {
        return service.updateCoupon(id, coupon);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCoupon(id);
    }

    @PostMapping("/applicable-coupons")
    public List<DiscountResponse> applicable(@RequestBody CartRequest cart) {
        return service.applicableCoupons(cart);
    }

    @PostMapping("/apply-coupon/{id}")
    public CartResponse apply(@PathVariable Long id, @RequestBody CartRequest cart) {
        return service.applyCoupon(id, cart);
    }
}