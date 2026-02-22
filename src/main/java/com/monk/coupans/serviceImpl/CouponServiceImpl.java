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
import com.monk.coupans.wrapperDto.ApplicableCouponResponseWrapper;
import com.monk.coupans.wrapperDto.CartRequestWrapper;
import com.monk.coupans.wrapperDto.CartResponseWrapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Service implementation containing business logic for coupons.
 *
 * Responsibilities:
 * - Coupon creation and validation
 * - Discount calculation
 * - Fetching applicable coupons
 * - Concurrency-safe coupon application
 *
 * Uses Strategy Pattern for handling different coupon types.
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepo;
    private final CartCouponRepository cartRepo;
    private final ProductCouponRepository productRepo;
    private final BxGyRepository bxgyRepo;
    private final StrategyFactory factory;

    /*
     * Creates coupon and corresponding subtype record.
     * Validates input and prevents duplicate coupons.
     */
    @Override
    public CouponResponse createCouponFromRequest(CouponCreateRequest request) {

        CouponType type = CouponType.valueOf(request.getType().toUpperCase());

        Coupon coupon = new Coupon();
        coupon.setType(type);
        coupon.setExpiryDate(request.getExpiryDate());
        coupon = couponRepo.save(coupon);

        Map<String, Object> details = request.getDetails();

        CouponResponse response = new CouponResponse();
        response.setId(coupon.getId());
        response.setType(coupon.getType().name());
        response.setExpiryDate(coupon.getExpiryDate());


        switch (type) {

            case CART_WISE -> {
                double threshold = Double.parseDouble(details.get("threshold").toString());
                double discount = Double.parseDouble(details.get("discount").toString());

                if(cartRepo.existsByThresholdAndDiscount(threshold, discount)) {
                    throw new InvalidCouponException("Duplicate cart-wise coupon");
                }

                CartCoupon cc = new CartCoupon();
                cc.setCoupon(coupon);
                cc.setThreshold(threshold);
                cc.setDiscount(discount);
                cartRepo.save(cc);

                response.setThreshold(threshold);
                response.setDiscount(discount);
            }

            case PRODUCT_WISE -> {
                long productId = Long.parseLong(details.get("product_id").toString());
                double discount = Double.parseDouble(details.get("discount").toString());

                if(productRepo.existsByProductIdAndDiscount(productId, discount)) {
                    throw new InvalidCouponException("Duplicate product-wise coupon");
                }

                ProductCoupon pc = new ProductCoupon();
                pc.setCoupon(coupon);
                pc.setProductId(productId);
                pc.setDiscount(discount);
                productRepo.save(pc);

                response.setProductId(productId);
                response.setDiscount(discount);
            }

            case BXGY -> {
                int repetitionLimit = Integer.parseInt(details.get("repition_limit").toString());

                List<Map<String, Object>> buyList =
                        (List<Map<String, Object>>) details.get("buy_products");

                List<Map<String, Object>> getList =
                        (List<Map<String, Object>>) details.get("get_products");

                List<BxGyCoupon> candidates =
                        bxgyRepo.findByLimitWithProducts(repetitionLimit);


                for (BxGyCoupon existing : candidates) {

                    boolean buyMatch = compareBuyProducts(existing.getBuyProducts(), buyList);
                    boolean getMatch = compareGetProducts(existing.getGetProducts(), getList);

                    if (buyMatch && getMatch) {
                        throw new InvalidCouponException("Duplicate BXGY coupon already exists");
                    }
                }

                BxGyCoupon bx = new BxGyCoupon();
                bx.setCoupon(coupon);
                bx.setRepetitionLimit(repetitionLimit);

                List<BxGyBuyProduct> buys = new ArrayList<>();

                for (Map<String, Object> map : buyList) {

                    BxGyBuyProduct b = new BxGyBuyProduct();
                    b.setProductId(Long.valueOf(map.get("product_id").toString()));
                    b.setQuantity(Integer.parseInt(map.get("quantity").toString()));
                    b.setBxGyCoupon(bx);

                    buys.add(b);
                }

                bx.setBuyProducts(buys);
                List<BxGyGetProduct> gets = new ArrayList<>();

                for (Map<String, Object> map : getList) {

                    BxGyGetProduct g = new BxGyGetProduct();
                    g.setProductId(Long.valueOf(map.get("product_id").toString()));
                    g.setQuantity(Integer.parseInt(map.get("quantity").toString()));
                    g.setBxGyCoupon(bx);

                    gets.add(g);
                }

                bx.setGetProducts(gets);

                bxgyRepo.save(bx);

                response.setRepetitionLimit(repetitionLimit);
                response.setBuyProducts(
                        buyList.stream().map( b -> new ProductQtyDTO(
                                Long.valueOf(b.get("product_id").toString()),
                                Integer.parseInt(b.get("quantity").toString())
                        )).toList()
                );
                response.setGetProducts(
                        getList.stream().map( g -> new ProductQtyDTO(
                                Long.valueOf(g.get("product_id").toString()),
                                Integer.parseInt(g.get("quantity").toString())
                        )).toList()
                );

            }
        }

        return response;
    }

    /*
     * Retrieves all coupons with subtype data.
     */
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

    /*
     * Fetches coupon by ID or throws exception if not found.
     */
    @Override
    public CouponResponse getCoupon(Long id) {
        Coupon coupon = couponRepo.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        return buildResponse(coupon);
    }

    /*
     * Updates coupon expiry or attributes.
     */
    @Override
    @Transactional
    public CouponResponse updateCoupon(Long id, CouponCreateRequest request) {

        Coupon existing = couponRepo.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        Map<String, Object> details = request.getDetails();

        // ================= UPDATE BASE FIELDS =================

        if (request.getExpiryDate() != null) {
            existing.setExpiryDate(request.getExpiryDate());
        }

        couponRepo.save(existing);

        // ================= UPDATE SUBTYPE =================

        switch (existing.getType()) {

            case CART_WISE -> {
                CartCoupon cart = cartRepo.findByCouponId(existing.getId());

                if (details.containsKey("threshold")) {
                    cart.setThreshold(Double.parseDouble(details.get("threshold").toString()));
                }

                if (details.containsKey("discount")) {
                    cart.setDiscount(Double.parseDouble(details.get("discount").toString()));
                }

                cartRepo.save(cart);
            }

            case PRODUCT_WISE -> {
                ProductCoupon product = productRepo.findByCouponId(existing.getId());
                if (details.containsKey("product_id")) {
                    product.setProductId(Long.parseLong(details.get("product_id").toString()));
                }

                if (details.containsKey("discount")) {
                    product.setDiscount(Double.parseDouble(details.get("discount").toString()));
                }

                productRepo.save(product);
            }

            case BXGY -> {
                BxGyCoupon bxgy = bxgyRepo.findByCouponId(existing.getId());

                if (details.containsKey("repition_limit")) {
                    bxgy.setRepetitionLimit(
                            Integer.parseInt(details.get("repition_limit").toString())
                    );
                }

                bxgyRepo.save(bxgy);
            }
        }

        return buildResponse(existing);
    }

    /*
     * Deletes coupon if exists.
     */
    @Override
    public void deleteCoupon(Long id) {
        couponRepo.deleteById(id);
    }

    /*
     * Returns all coupons applicable for a given cart.
     * Each coupon is validated using its respective strategy.
     */
    @Override
    public ApplicableCouponResponseWrapper applicableCoupons(CartRequestWrapper cartWrapper) {
        List<DiscountResponse> result = new ArrayList<>();
        CartRequest cart = cartWrapper.getCart();
        for (Coupon coupon : couponRepo.findAll()) {
            CouponStrategy strategy = factory.getStrategy(coupon.getType());
            if (strategy.isApplicable(cart, coupon)) {
                double discount = strategy.calculateDiscount(cart, coupon);
                result.add(new DiscountResponse(coupon.getId(), coupon.getType(), discount));
            }
        }

        ApplicableCouponResponseWrapper wrapper = new ApplicableCouponResponseWrapper();
        wrapper.setApplicableCouponResponses(result);

        return wrapper;
    }

    /*
     * Applies a coupon to the cart.
     *
     * Steps:
     * 1. Validate cart and coupon
     * 2. Check expiry and usage limits
     * 3. Calculate discount using strategy
     * 4. Safely decrement usage limit
     *
     * Transactional to ensure atomic updates.
     * Uses optimistic locking to prevent race conditions.
     */
    @Transactional
    @Override
    public CartResponseWrapper applyCoupon(Long id, CartRequestWrapper cartWrapper) {


        Coupon coupon = couponRepo.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        // Validate coupon is not expired
        if (coupon.getExpiryDate()!=null &&
                coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidCouponException("Coupon expired");
        }

        CartRequest cart = cartWrapper.getCart();

        // Fetch correct strategy based on coupon type
        CouponStrategy strategy = factory.getStrategy(coupon.getType());
        if (!strategy.isApplicable(cart, coupon)) {
            throw new InvalidCouponException("Coupon not applicable");
        }

        List<CartItemResponse> cartItemResponses = new ArrayList<>();

        double totalPrice=0;
        double totalDiscount=0;
        for (CartItem item : cart.getItems()) {

            double itemTotal = item.getPrice() * item.getQuantity();
            totalPrice += itemTotal;

            double itemDiscount = strategy.calculateDiscount(
                    new CartRequest(List.of(item)), coupon
            );

            totalDiscount += itemDiscount;

            cartItemResponses.add(new CartItemResponse(
                    item.getProductId(),
                    item.getQuantity(),
                    item.getPrice(),
                    itemDiscount
            ));
        }

        double finalPrice = totalPrice - totalDiscount;

        CartResponse updated = new CartResponse(
                cartItemResponses,
                totalPrice,
                totalDiscount,
                finalPrice
        );

        return new CartResponseWrapper(updated);

    }

    private CouponResponse buildResponse(Coupon coupon) {

        CouponResponse res = new CouponResponse();

        res.setId(coupon.getId());
        res.setType(coupon.getType().name());
        res.setExpiryDate(coupon.getExpiryDate());

        switch (coupon.getType()) {

            case CART_WISE -> {
                CartCoupon cc = cartRepo.findByCouponId(coupon.getId());
                if (cc != null) {
                    res.setThreshold(cc.getThreshold());
                    res.setDiscount(cc.getDiscount());
                }
            }

            case PRODUCT_WISE -> {
                ProductCoupon pc = productRepo.findByCouponId(coupon.getId());
                if (pc != null) {
                    res.setProductId(pc.getProductId());
                    res.setDiscount(pc.getDiscount());
                }
            }

            case BXGY -> {
                BxGyCoupon bx = bxgyRepo.findByCouponId(coupon.getId());
                if (bx != null) {
                    res.setRepetitionLimit(bx.getRepetitionLimit());
                }
            }
        }

        return res;
    }

    private boolean compareBuyProducts(List<BxGyBuyProduct> dbList,
                                       List<Map<String, Object>> reqList) {

        if (dbList.size() != reqList.size()) return false;

        for (Map<String, Object> req : reqList) {

            Long productId = Long.valueOf(req.get("product_id").toString());
            int qty = Integer.parseInt(req.get("quantity").toString());

            boolean match = dbList.stream().anyMatch(db ->
                    db.getProductId().equals(productId)
                            && db.getQuantity() == qty
            );

            if (!match) return false;
        }

        return true;
    }

    private boolean compareGetProducts(List<BxGyGetProduct> dbList,
                                       List<Map<String, Object>> reqList) {

        if (dbList.size() != reqList.size()) return false;

        for (Map<String, Object> req : reqList) {

            Long productId = Long.valueOf(req.get("product_id").toString());
            int qty = Integer.parseInt(req.get("quantity").toString());

            boolean match = dbList.stream().anyMatch(db ->
                    db.getProductId().equals(productId)
                            && db.getQuantity() == qty
            );

            if (!match) return false;
        }

        return true;
    }
}