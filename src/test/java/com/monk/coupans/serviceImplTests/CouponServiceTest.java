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
        request.setType("CART_WISE");

        Map<String,Object> details = new HashMap<>();
        details.put("threshold", 100);
        details.put("discount", 10);
        request.setDetails(details);

        Coupon saved = new Coupon();
        saved.setId(1L);

        when(couponRepo.save(any())).thenReturn(saved);

        Coupon result = service.createCouponFromRequest(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(cartRepo, times(1)).save(any(CartCoupon.class));
    }


    @Test
    void shouldCreateProductWiseCouponSuccessfully() {

        CouponCreateRequest request = new CouponCreateRequest();
        request.setType("PRODUCT_WISE");

        Map<String,Object> details = new HashMap<>();
        details.put("product_id", 5L);
        details.put("discount", 20);
        request.setDetails(details);

        Coupon saved = new Coupon();
        saved.setId(2L);

        when(couponRepo.save(any())).thenReturn(saved);

        Coupon result = service.createCouponFromRequest(request);

        assertEquals(2L, result.getId());
        verify(productRepo).save(any(ProductCoupon.class));
    }


    @Test
    void shouldApplyCouponSuccessfully() {

        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setType(CouponType.CART_WISE);
        coupon.setUsageLimit(5);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(2));

        when(couponRepo.findById(1L)).thenReturn(Optional.of(coupon));
        when(factory.getStrategy(any())).thenReturn(strategy);
        when(strategy.isApplicable(any(), any())).thenReturn(true);
        when(strategy.calculateDiscount(any(), any())).thenReturn(50.0);

        CartItem item = new CartItem(1L,2,100);
        CartRequest cart = new CartRequest(List.of(item));

        CartResponse response = service.applyCoupon(1L, cart);

        assertEquals(50.0, response.getDiscount());
        assertEquals(150.0, response.getFinalPrice());

        verify(couponRepo).save(coupon);
    }


    @Test
    void shouldThrowExceptionIfCouponExpired() {

        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setExpiryDate(LocalDateTime.now().minusDays(1));

        when(couponRepo.findById(1L)).thenReturn(Optional.of(coupon));

        assertThrows(InvalidCouponException.class,
                () -> service.applyCoupon(1L, new CartRequest()));
    }


    @Test
    void shouldThrowExceptionIfUsageLimitExceeded() {

        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setUsageLimit(0);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(5));

        when(couponRepo.findById(1L)).thenReturn(Optional.of(coupon));

        assertThrows(InvalidCouponException.class,
                () -> service.applyCoupon(1L, new CartRequest()));
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

        List<DiscountResponse> result = service.applicableCoupons(cart);

        assertEquals(1, result.size());
        assertEquals(25.0, result.get(0).getDiscount());
    }
}