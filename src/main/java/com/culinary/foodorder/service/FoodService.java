package com.culinary.foodorder.service;

import com.culinary.foodorder.dto.request.FoodRequest;
import com.culinary.foodorder.dto.response.FoodResponse;
import com.culinary.foodorder.entity.Food;
import com.culinary.foodorder.exception.ResourceNotFoundException;
import com.culinary.foodorder.repository.FoodRepository;
import com.culinary.foodorder.util.DtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FoodService {

    private static final Logger log = LoggerFactory.getLogger(FoodService.class);

    private final FoodRepository foodRepository;
    private final FileStorageService fileStorageService;

    public FoodService(FoodRepository foodRepository, FileStorageService fileStorageService) {
        this.foodRepository = foodRepository;
        this.fileStorageService = fileStorageService;
    }

    // ===== PUBLIC ENDPOINTS =====

    public Page<FoodResponse> getAllFoods(int page, int size) {
        log.info("Fetching all foods - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return foodRepository.findAll(pageable).map(DtoMapper::toFoodResponse);
    }

    public Page<FoodResponse> getFoodsByCategory(String category, int page, int size) {
        log.info("Fetching foods by category: {} - page: {}, size: {}", category, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return foodRepository.findByCategory(category, pageable).map(DtoMapper::toFoodResponse);
    }

    public Page<FoodResponse> searchFoods(String search, String category, int page, int size) {
        log.info("Searching foods - search: {}, category: {}, page: {}, size: {}", search, category, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        if (category != null && !category.isBlank()) {
            return foodRepository.searchByCategoryAndNameOrDescription(category, search, pageable)
                    .map(DtoMapper::toFoodResponse);
        }
        return foodRepository.searchByNameOrDescription(search, pageable).map(DtoMapper::toFoodResponse);
    }

    public FoodResponse getFoodById(Long id) {
        log.info("Fetching food by ID: {}", id);
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food", "id", id));
        return DtoMapper.toFoodResponse(food);
    }

    // ===== ADMIN ENDPOINTS =====

    @Transactional
    public FoodResponse createFood(FoodRequest request, MultipartFile image) {
        log.info("Creating new food item: {}", request.getName());

        Food food = Food.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(image);
            food.setImageUrl(imageUrl);
            log.info("Image uploaded for food: {}", imageUrl);
        }

        Food saved = foodRepository.save(food);
        log.info("Food created successfully with ID: {}", saved.getId());
        return DtoMapper.toFoodResponse(saved);
    }

    @Transactional
    public FoodResponse updateFood(Long id, FoodRequest request, MultipartFile image) {
        log.info("Updating food item ID: {}", id);

        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food", "id", id));

        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setCategory(request.getCategory());

        if (image != null && !image.isEmpty()) {
            // Delete old image if exists
            if (food.getImageUrl() != null) {
                fileStorageService.deleteFile(food.getImageUrl());
            }
            String imageUrl = fileStorageService.storeFile(image);
            food.setImageUrl(imageUrl);
            log.info("Image updated for food: {}", imageUrl);
        }

        Food saved = foodRepository.save(food);
        log.info("Food updated successfully: {}", saved.getId());
        return DtoMapper.toFoodResponse(saved);
    }

    @Transactional
    public void deleteFood(Long id) {
        log.info("Deleting food item ID: {}", id);

        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food", "id", id));

        // Delete image file
        if (food.getImageUrl() != null) {
            fileStorageService.deleteFile(food.getImageUrl());
        }

        foodRepository.delete(food);
        log.info("Food deleted successfully: {}", id);
    }
}
