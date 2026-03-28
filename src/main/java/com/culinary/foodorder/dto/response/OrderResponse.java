package com.culinary.foodorder.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private String customerName;
    private String phoneNumber;
    private String location;
    private String status;
    private String orderType;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
}
