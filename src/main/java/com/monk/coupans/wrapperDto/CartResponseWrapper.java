package com.monk.coupans.wrapperDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monk.coupans.dto.CartResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseWrapper {
    @JsonProperty("updated_cart")
    private CartResponse response;
}
