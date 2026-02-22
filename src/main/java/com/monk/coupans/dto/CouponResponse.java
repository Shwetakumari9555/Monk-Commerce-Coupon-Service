package com.monk.coupans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponResponse {

    private Long id;
    private String type;
    private LocalDateTime expiryDate;

    // CART
    private Double threshold;
    private Double discount;

    //PRODUCT
    private Long productId;

    //BXGY
    private Integer repetitionLimit;
    private List<ProductQtyDTO> buyProducts;
    private List<ProductQtyDTO> getProducts;
}