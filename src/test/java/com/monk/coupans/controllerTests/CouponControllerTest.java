package com.monk.coupans.controllerTests;


import com.monk.coupans.controller.CouponController;
import com.monk.coupans.dto.*;
import com.monk.coupans.entity.Coupon;
import com.monk.coupans.entity.CouponType;
import com.monk.coupans.service.CouponService;
import com.monk.coupans.wrapperDto.CartRequestWrapper;
import com.monk.coupans.wrapperDto.CartResponseWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

    @InjectMocks
    private CouponController controller;

    @Mock
    private CouponService service;


    @Test
    void shouldCreateCouponSuccessfully() {

        CouponCreateRequest request = new CouponCreateRequest();
        request.setType("CART_WISE");
        request.setDetails(Map.of("threshold",100,"discount",10));

        CouponResponse saved = new CouponResponse();
        saved.setId(1L);
        saved.setType(String.valueOf(CouponType.CART_WISE));
        saved.setThreshold(100.00);
        saved.setDiscount(10.00);

        when(service.createCouponFromRequest(request)).thenReturn(saved);

        CouponResponse response = controller.create(request);

        assertEquals(1L, response.getId());
        verify(service).createCouponFromRequest(request);
    }


    @Test
    void shouldReturnAllCoupons() {

        CouponResponse response = new CouponResponse();
        response.setId(1L);
        response.setType("CART_WISE");

        when(service.getAllCoupons()).thenReturn(List.of(response));

        List<CouponResponse> result = controller.getAll();

        assertEquals(1, result.size());
        assertEquals("CART_WISE", result.get(0).getType());
    }


    @Test
    void shouldReturnCouponById() {

        CouponResponse coupon = new CouponResponse();
        coupon.setId(1L);

        when(service.getCoupon(1L)).thenReturn(coupon);

        CouponResponse result = controller.get(1L);

        assertEquals(1L, result.getId());
    }


    @Test
    void shouldApplyCouponSuccessfully() {

        CartItem item = new CartItem(1L,2,100);
        CartRequest cart = new CartRequest(List.of(item));
        CartRequestWrapper cartRequestWrapper = new CartRequestWrapper(cart);

        CartResponseWrapper cartResponseWrapper = getCartResponseWrapper();

        when(service.applyCoupon(1L, cartRequestWrapper)).thenReturn(cartResponseWrapper);

        CartResponseWrapper result = controller.apply(1L, cartRequestWrapper);

        assertEquals(180, result.getResponse().getFinalPrice());
        verify(service).applyCoupon(1L, cartRequestWrapper);
    }

    private static CartResponseWrapper getCartResponseWrapper() {
        List<CartItemResponse> items = new ArrayList<>();
        CartItemResponse response = new CartItemResponse();
        response.setPrice(100);
        response.setQuantity(2);
        response.setProductId(1L);
        response.setTotalDiscount(20);
        items.add(response);

        CartResponse cartResponse = new CartResponse(items,200,20,180);
        CartResponseWrapper cartResponseWrapper = new CartResponseWrapper(cartResponse);
        cartResponseWrapper.setResponse(cartResponse);
        return cartResponseWrapper;
    }


    @Test
    void shouldDeleteCoupon() {

        doNothing().when(service).deleteCoupon(1L);

        controller.delete(1L);

        verify(service).deleteCoupon(1L);
    }
}