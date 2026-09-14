package com.blinkit.application.order.services;

import com.blinkit.application.admin.dtos.response.AdminOrderResponse;
import com.blinkit.application.order.dtos.requests.PlaceOrderRequest;
import com.blinkit.application.order.dtos.response.OrderResponse;
import com.blinkit.application.order.dtos.response.OrderSummaryResponse;
import com.blinkit.application.order.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(Long userId, PlaceOrderRequest request);

    List<OrderSummaryResponse> getOrderHistory(Long userId);

    OrderResponse getOrderDetail(Long userId, Long orderId);

    List<AdminOrderResponse> getAllOrdersForAdmin();

    void updateOrderStatus(Long orderId, OrderStatus status);

}

