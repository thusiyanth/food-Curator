package com.culinary.foodorder.service;

import com.culinary.foodorder.dto.request.OrderItemRequest;
import com.culinary.foodorder.dto.request.OrderRequest;
import com.culinary.foodorder.dto.response.DashboardStatsResponse;
import com.culinary.foodorder.dto.response.OrderResponse;
import com.culinary.foodorder.entity.Food;
import com.culinary.foodorder.entity.Order;
import com.culinary.foodorder.entity.OrderItem;
import com.culinary.foodorder.exception.BadRequestException;
import com.culinary.foodorder.exception.ResourceNotFoundException;
import com.culinary.foodorder.repository.FoodRepository;
import com.culinary.foodorder.repository.OrderRepository;
import com.culinary.foodorder.util.DtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;

    public OrderService(OrderRepository orderRepository, FoodRepository foodRepository) {
        this.orderRepository = orderRepository;
        this.foodRepository = foodRepository;
    }

    // ===== PUBLIC ENDPOINTS =====

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating new order for customer: {}", request.getCustomerName());

        String orderType = request.getOrderType() != null ? request.getOrderType() : "REGULAR";

        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .phoneNumber(request.getPhoneNumber())
                .location(request.getLocation())
                .status("PENDING")
                .orderType(orderType)
                .build();

        for (OrderItemRequest itemReq : request.getItems()) {
            Food food = foodRepository.findById(itemReq.getFoodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Food", "id", itemReq.getFoodId()));

            OrderItem item = OrderItem.builder()
                    .food(food)
                    .quantity(itemReq.getQuantity())
                    .build();

            order.addItem(item);
        }

        Order saved = orderRepository.save(order);
        log.info("Order created successfully with ID: {} | Type: {} | Items: {}",
                saved.getId(), orderType, request.getItems().size());
        return DtoMapper.toOrderResponse(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order by ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return DtoMapper.toOrderResponse(order);
    }

    // ===== ADMIN ENDPOINTS =====

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(String status, String orderType, int page, int size) {
        log.info("Fetching orders - status: {}, type: {}, page: {}, size: {}", status, orderType, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        if (status != null && !status.isBlank() && orderType != null && !orderType.isBlank()) {
            return orderRepository.findByStatusAndOrderType(status.toUpperCase(), orderType.toUpperCase(), pageable)
                    .map(DtoMapper::toOrderResponse);
        }
        if (status != null && !status.isBlank()) {
            return orderRepository.findByStatus(status.toUpperCase(), pageable)
                    .map(DtoMapper::toOrderResponse);
        }
        if (orderType != null && !orderType.isBlank()) {
            return orderRepository.findByOrderType(orderType.toUpperCase(), pageable)
                    .map(DtoMapper::toOrderResponse);
        }
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(DtoMapper::toOrderResponse);
    }

    @Transactional
    public OrderResponse approveOrder(Long id) {
        log.info("Approving order ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        if (!"PENDING".equals(order.getStatus())) {
            throw new BadRequestException("Only PENDING orders can be approved. Current status: " + order.getStatus());
        }

        order.setStatus("APPROVED");
        Order saved = orderRepository.save(order);
        log.info("Order {} approved successfully", id);
        return DtoMapper.toOrderResponse(saved);
    }

    @Transactional
    public OrderResponse rejectOrder(Long id) {
        log.info("Rejecting order ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        if (!"PENDING".equals(order.getStatus())) {
            throw new BadRequestException("Only PENDING orders can be rejected. Current status: " + order.getStatus());
        }

        order.setStatus("REJECTED");
        Order saved = orderRepository.save(order);
        log.info("Order {} rejected", id);
        return DtoMapper.toOrderResponse(saved);
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        log.info("Fetching dashboard statistics");
        return DashboardStatsResponse.builder()
                .totalOrders(orderRepository.count())
                .pendingOrders(orderRepository.countByStatus("PENDING"))
                .approvedOrders(orderRepository.countByStatus("APPROVED"))
                .rejectedOrders(orderRepository.countByStatus("REJECTED"))
                .totalFoodItems(foodRepository.count())
                .buffetOrders(orderRepository.countByOrderType("BUFFET"))
                .build();
    }
}
