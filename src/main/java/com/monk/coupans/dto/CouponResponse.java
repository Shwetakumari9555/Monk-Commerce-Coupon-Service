package com.monk.coupans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponResponse {

    private Long id;
    private String type;
    private LocalDateTime expiryDate;
    private Integer usageLimit;

    private Double threshold;
    private Double discount;
    private Long productId;
    private Integer repetitionLimit;
}