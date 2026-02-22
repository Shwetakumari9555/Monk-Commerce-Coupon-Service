package com.monk.coupans.controller;

import com.monk.coupans.dto.*;
import com.monk.coupans.entity.Coupon;
import com.monk.coupans.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
 * REST Controller that exposes APIs for managing coupons
 * and applying them to shopping carts.
 */
@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService service;

    /*
     * Creates a new coupon based on request details.
     */
    @PostMapping("/create")
    public Coupon create(@RequestBody CouponCreateRequest request) {
        return service.createCouponFromRequest(request);
    }

    /*
     * Returns all coupons with subtype details.
     */
    @GetMapping("/fetchAll")
    public List<CouponResponse> getAll() {
        return service.getAllCoupons();
    }

    /*
     * Fetches a specific coupon by ID.
     */
    @GetMapping("/{id}")
    public CouponResponse get(@PathVariable Long id) {
        return service.getCoupon(id);
    }

    /*
     * Updates expiry date or other coupon attributes.
     */
    @PutMapping("/{id}")
    public CouponResponse update(@PathVariable Long id, @RequestBody CouponCreateRequest coupon) {
        return service.updateCoupon(id, coupon);
    }

    /*
     * Deletes a coupon by ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCoupon(id);
    }

    /*
     * Returns all coupons applicable to a given cart.
     */
    @PostMapping("/applicableCoupons")
    public List<DiscountResponse> applicable(@RequestBody CartRequest cart) {
        return service.applicableCoupons(cart);
    }

    /*
     * Applies a specific coupon to the cart and returns the updated cart details.
     */
    @PostMapping("/applyCoupon/{id}")
    public CartResponse apply(@PathVariable Long id, @RequestBody CartRequest cart) {
        return service.applyCoupon(id, cart);
    }
}