package com.monk.coupans.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

   @JsonProperty("product_id")
   private Long productId;

   private int quantity;

   private double price;

   @JsonProperty("total_discount")
   private double totalDiscount;
}
