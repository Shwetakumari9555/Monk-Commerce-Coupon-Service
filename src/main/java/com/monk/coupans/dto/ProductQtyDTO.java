package com.monk.coupans.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQtyDTO {
    private Long productId;
    private int quantity;
}
