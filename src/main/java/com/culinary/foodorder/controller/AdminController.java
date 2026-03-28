package com.culinary.foodorder.controller;

import com.culinary.foodorder.dto.request.FoodRequest;
import com.culinary.foodorder.dto.response.ApiResponse;
import com.culinary.foodorder.dto.response.DashboardStatsResponse;
import com.culinary.foodorder.dto.response.FoodResponse;
import com.culinary.foodorder.dto.response.OrderResponse;
import com.culinary.foodorder.service.FoodService;
import com.culinary.foodorder.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final FoodService foodService;
    private final OrderService orderService;

    public AdminController(FoodService foodService, OrderService orderService) {
        this.foodService = foodService;
        this.orderService = orderService;
    }

    // ===== DASHBOARD =====

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        log.info("GET /api/admin/dashboard/stats");
        DashboardStatsResponse stats = orderService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats retrieved", stats));
    }

    // ===== FOOD MANAGEMENT =====

    @PostMapping(value = "/foods", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> createFood(
            @Valid @RequestPart("food") FoodRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        log.info("POST /api/admin/foods - name: {}", request.getName());
        FoodResponse food = foodService.createFood(request, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Food item created successfully", food));
    }

    @PutMapping(value = "/foods/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FoodResponse>> updateFood(
            @PathVariable Long id,
            @Valid @RequestPart("food") FoodRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        log.info("PUT /api/admin/foods/{}", id);
        FoodResponse food = foodService.updateFood(id, request, image);
        return ResponseEntity.ok(ApiResponse.success("Food item updated successfully", food));
    }

    @DeleteMapping("/foods/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFood(@PathVariable Long id) {
        log.info("DELETE /api/admin/foods/{}", id);
        foodService.deleteFood(id);
        return ResponseEntity.ok(ApiResponse.success("Food item deleted successfully"));
    }

    // ===== ORDER MANAGEMENT =====

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String orderType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/admin/orders - status: {}, type: {}, page: {}, size: {}", status, orderType, page, size);
        Page<OrderResponse> orders = orderService.getAllOrders(status, orderType, page, size);
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
    }

    @PutMapping("/orders/{id}/approve")
    public ResponseEntity<ApiResponse<OrderResponse>> approveOrder(@PathVariable Long id) {
        log.info("PUT /api/admin/orders/{}/approve", id);
        OrderResponse order = orderService.approveOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order approved successfully", order));
    }

    @PutMapping("/orders/{id}/reject")
    public ResponseEntity<ApiResponse<OrderResponse>> rejectOrder(@PathVariable Long id) {
        log.info("PUT /api/admin/orders/{}/reject", id);
        OrderResponse order = orderService.rejectOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order rejected successfully", order));
    }
}
