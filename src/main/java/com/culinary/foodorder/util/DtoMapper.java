package com.culinary.foodorder.util;

import com.culinary.foodorder.dto.response.*;
import com.culinary.foodorder.entity.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DtoMapper {

    private DtoMapper() {
        // Utility class — prevent instantiation
    }

    public static FoodResponse toFoodResponse(Food food) {
        return FoodResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .description(food.getDescription())
                .price(food.getPrice())
                .category(food.getCategory())
                .imageUrl(food.getImageUrl())
                .createdAt(food.getCreatedAt())
                .build();
    }

    public static OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream().filter(Objects::nonNull)
                .map(DtoMapper::toOrderItemResponse)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .phoneNumber(order.getPhoneNumber())
                .location(order.getLocation())
                .status(order.getStatus())
                .orderType(order.getOrderType())
                .items(itemResponses)
                .createdAt(order.getCreatedAt())
                .build();
    }

    public static OrderItemResponse toOrderItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .foodId(item.getFood().getId())
                .foodName(item.getFood().getName())
                .foodPrice(item.getFood().getPrice())
                .foodImageUrl(item.getFood().getImageUrl())
                .quantity(item.getQuantity())
                .build();
    }
}
