package com.monk.coupans.wrapperDto;

import com.monk.coupans.dto.CartRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestWrapper {
    CartRequest cart;
}
