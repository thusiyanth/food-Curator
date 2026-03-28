package com.culinary.foodorder.controller;

import com.culinary.foodorder.dto.response.ApiResponse;
import com.culinary.foodorder.dto.response.FoodResponse;
import com.culinary.foodorder.service.FoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private static final Logger log = LoggerFactory.getLogger(FoodController.class);

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FoodResponse>>> getAllFoods(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/foods - category: {}, search: {}, page: {}, size: {}", category, search, page, size);

        Page<FoodResponse> foods;

        if (search != null && !search.isBlank()) {
            foods = foodService.searchFoods(search, category, page, size);
        } else if (category != null && !category.isBlank()) {
            foods = foodService.getFoodsByCategory(category, page, size);
        } else {
            foods = foodService.getAllFoods(page, size);
        }

        return ResponseEntity.ok(ApiResponse.success("Foods retrieved successfully", foods));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodResponse>> getFoodById(@PathVariable Long id) {
        log.info("GET /api/foods/{}", id);
        FoodResponse food = foodService.getFoodById(id);
        return ResponseEntity.ok(ApiResponse.success("Food retrieved successfully", food));
    }
}
