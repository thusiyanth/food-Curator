package com.culinary.foodorder.controller;

import com.culinary.foodorder.dto.request.OrderRequest;
import com.culinary.foodorder.dto.response.ApiResponse;
import com.culinary.foodorder.dto.response.OrderResponse;
import com.culinary.foodorder.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest request) {
        log.info("POST /api/orders - customer: {}, items: {}", request.getCustomerName(), request.getItems().size());
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order placed successfully", order));
    }

    @PostMapping("/buffet")
    public ResponseEntity<ApiResponse<OrderResponse>> createBuffetOrder(@Valid @RequestBody OrderRequest request) {
        log.info("POST /api/orders/buffet - customer: {}", request.getCustomerName());
        request.setOrderType("BUFFET");
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Buffet order placed successfully", order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        log.info("GET /api/orders/{}", id);
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order));
    }
}
