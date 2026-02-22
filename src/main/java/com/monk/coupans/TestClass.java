package com.monk.coupans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monk.coupans.dto.CartItem;
import com.monk.coupans.dto.CartRequest;
import com.monk.coupans.wrapperDto.CartRequestWrapper;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

public class TestClass {
    public static void main(String[] args) {
        CartItem item = new CartItem(1L, 2, 10.0);
        CartRequest cartRequest = new CartRequest();
        cartRequest.setItems(java.util.Collections.singletonList(item));

        CartRequestWrapper wrapper = new CartRequestWrapper(cartRequest);

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(wrapper);
        System.out.println(payload);
    }

}
