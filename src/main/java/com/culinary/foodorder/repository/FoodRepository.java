package com.culinary.foodorder.repository;

import com.culinary.foodorder.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    Page<Food> findByCategory(String category, Pageable pageable);

    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(f.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Food> searchByNameOrDescription(@Param("search") String search, Pageable pageable);

    @Query("SELECT f FROM Food f WHERE f.category = :category AND " +
           "(LOWER(f.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(f.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Food> searchByCategoryAndNameOrDescription(
            @Param("category") String category,
            @Param("search") String search,
            Pageable pageable);

    long countByCategory(String category);
}
