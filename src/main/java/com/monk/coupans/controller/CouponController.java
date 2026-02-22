package com.monk.coupans.controller;

import com.monk.coupans.dto.*;
import com.monk.coupans.service.CouponService;
import com.monk.coupans.wrapperDto.ApplicableCouponResponseWrapper;
import com.monk.coupans.wrapperDto.CartRequestWrapper;
import com.monk.coupans.wrapperDto.CartResponseWrapper;
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
    public CouponResponse create(@RequestBody CouponCreateRequest request) {
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
    @PostMapping("/applicable-coupons")
    public ApplicableCouponResponseWrapper applicable(@RequestBody CartRequestWrapper cart) {
        return service.applicableCoupons(cart);
    }

    /*
     * Applies a specific coupon to the cart and returns the updated cart details.
     */
    @PostMapping("/apply-coupon/{id}")
    public CartResponseWrapper apply(@PathVariable Long id, @RequestBody CartRequestWrapper cart) {
        return service.applyCoupon(id, cart);
    }
}