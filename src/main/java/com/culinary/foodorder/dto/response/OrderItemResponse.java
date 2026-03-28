package com.culinary.foodorder.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long id;
    private Long foodId;
    private String foodName;
    private BigDecimal foodPrice;
    private String foodImageUrl;
    private Integer quantity;
}
