package com.monk.coupans.serviceImplTests;

import com.monk.coupans.dto.*;
import com.monk.coupans.entity.CartCoupon;
import com.monk.coupans.entity.Coupon;
import com.monk.coupans.entity.CouponType;
import com.monk.coupans.entity.ProductCoupon;
import com.monk.coupans.exceptions.InvalidCouponException;
import com.monk.coupans.repository.BxGyRepository;
import com.monk.coupans.repository.CartCouponRepository;
import com.monk.coupans.repository.CouponRepository;
import com.monk.coupans.repository.ProductCouponRepository;
import com.monk.coupans.serviceImpl.CouponServiceImpl;
import com.monk.coupans.strategy.CouponStrategy;
import com.monk.coupans.strategy.StrategyFactory;
import com.monk.coupans.wrapperDto.ApplicableCouponResponseWrapper;
import com.monk.coupans.wrapperDto.CartRequestWrapper;
import com.monk.coupans.wrapperDto.CartResponseWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponServiceImpl service;

    @Mock
    private CouponRepository couponRepo;

    @Mock
    private CartCouponRepository cartRepo;

    @Mock
    private ProductCouponRepository productRepo;

    @Mock
    private BxGyRepository bxgyRepo;

    @Mock
    private StrategyFactory factory;

    @Mock
    private CouponStrategy strategy;


    @Test
    void shouldCreateCartWiseCouponSuccessfully() {

        CouponCreateRequest request = new CouponCreateRequest();
        request.setType(String.valueOf(CouponType.CART_WISE));

        Map<String,Object> details = new HashMap<>();
        details.put("threshold", 100);
        details.put("discount", 10);
        request.setDetails(details);

        Coupon saved = new Coupon();
        saved.setId(1L);
        saved.setType(CouponType.CART_WISE);


        when(couponRepo.save(any())).thenReturn(saved);

        CouponResponse result = service.createCouponFromRequest(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(cartRepo, times(1)).save(any(CartCoupon.class));
    }


    @Test
    void shouldCreateProductWiseCouponSuccessfully() {

        CouponCreateRequest request = new CouponCreateRequest();
        request.setType(String.valueOf(CouponType.PRODUCT_WISE));

        Map<String,Object> details = new HashMap<>();
        details.put("product_id", 5L);
        details.put("discount", 20);
        request.setDetails(details);

        Coupon saved = new Coupon();
        saved.setId(2L);
        saved.setType(CouponType.PRODUCT_WISE);

        when(couponRepo.save(any())).thenReturn(saved);

        CouponResponse result = service.createCouponFromRequest(request);

        assertEquals(2L, result.getId());
        verify(productRepo).save(any(ProductCoupon.class));
    }


    @Test
    void shouldApplyCouponSuccessfully() {

        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setType(CouponType.CART_WISE);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(2));

        when(couponRepo.findById(1L)).thenReturn(Optional.of(coupon));
        when(factory.getStrategy(any())).thenReturn(strategy);
        when(strategy.isApplicable(any(), any())).thenReturn(true);
        when(strategy.calculateDiscount(any(), any())).thenReturn(50.0);

        CartItem item = new CartItem(1L,2,100);
        CartRequest cart = new CartRequest(List.of(item));
        CartRequestWrapper wrapper = new CartRequestWrapper(cart);

        CartResponseWrapper response = service.applyCoupon(1L, wrapper);

        assertEquals(50.0, response.getResponse().getDiscount());
        assertEquals(150.0, response.getResponse().getFinalPrice());
    }


    @Test
    void shouldThrowExceptionIfCouponExpired() {

        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setExpiryDate(LocalDateTime.now().minusDays(1));

        when(couponRepo.findById(1L)).thenReturn(Optional.of(coupon));

        assertThrows(InvalidCouponException.class,
                () -> service.applyCoupon(1L, new CartRequestWrapper()));
    }



    @Test
    void shouldReturnApplicableCoupons() {

        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setType(CouponType.CART_WISE);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(2));

        when(couponRepo.findAll()).thenReturn(List.of(coupon));
        when(factory.getStrategy(any())).thenReturn(strategy);
        when(strategy.isApplicable(any(), any())).thenReturn(true);
        when(strategy.calculateDiscount(any(), any())).thenReturn(25.0);

        CartItem item = new CartItem(1L,1,100);
        CartRequest cart = new CartRequest(List.of(item));
        CartRequestWrapper wrapper = new CartRequestWrapper(cart);

        ApplicableCouponResponseWrapper result = service.applicableCoupons(wrapper);

        assertEquals(1, result.getApplicableCouponResponses().size());
        assertEquals(25.0, result.getApplicableCouponResponses().get(0).getDiscount());
    }
}