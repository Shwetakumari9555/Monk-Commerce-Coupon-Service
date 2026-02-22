package com.monk.coupans.wrapperDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monk.coupans.dto.DiscountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicableCouponResponseWrapper {
    @JsonProperty("applicable_coupons")
    private List<DiscountResponse> applicableCouponResponses;
}
