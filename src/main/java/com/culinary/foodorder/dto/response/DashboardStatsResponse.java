package com.culinary.foodorder.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {

    private long totalOrders;
    private long pendingOrders;
    private long approvedOrders;
    private long rejectedOrders;
    private long totalFoodItems;
    private long buffetOrders;
}
