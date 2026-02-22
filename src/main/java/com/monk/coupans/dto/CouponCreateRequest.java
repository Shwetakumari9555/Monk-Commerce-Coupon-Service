package com.monk.coupans.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCreateRequest {

    private String type;
    private LocalDateTime expiryDate;
    private Map<String, Object> details;
}
