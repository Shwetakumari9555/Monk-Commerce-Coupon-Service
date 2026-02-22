package com.monk.coupans.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private List<CartItemResponse> items;

    @JsonProperty("total_price")
    private double totalPrice;

    @JsonProperty("total_discount")
    private double discount;

    @JsonProperty("final_price")
    private double finalPrice;
}
