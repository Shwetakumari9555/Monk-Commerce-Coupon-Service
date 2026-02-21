package com.monk.coupans.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private double totalPrice;
    private double discount;
    private double finalPrice;
}
