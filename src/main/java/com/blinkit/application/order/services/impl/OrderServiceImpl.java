package com.blinkit.application.order.services.impl;

import com.blinkit.application.cart.dtos.response.AdminOrderResponse;
import com.blinkit.application.cart.dtos.response.CartItemResponse;
import com.blinkit.application.cart.dtos.response.CartResponse;
import com.blinkit.application.cart.exceptions.EmptyCartException;
import com.blinkit.application.cart.exceptions.InsufficientStockException;
import com.blinkit.application.cart.services.CartService;
import com.blinkit.application.order.dtos.response.OrderItemResponse;
import com.blinkit.application.order.dtos.requests.PlaceOrderRequest;
import com.blinkit.application.order.dtos.response.OrderResponse;
import com.blinkit.application.order.dtos.response.OrderSummaryResponse;
import com.blinkit.application.order.entities.Order;
import com.blinkit.application.order.entities.OrderItem;
import com.blinkit.application.order.enums.OrderStatus;
import com.blinkit.application.order.exceptions.OrderNotFoundException;
import com.blinkit.application.order.repositories.OrderRepository;
import com.blinkit.application.order.services.OrderService;
import com.blinkit.application.productAndCategory.entities.DarkStore;
import com.blinkit.application.productAndCategory.exceptions.DarkStoreNotFoundException;
import com.blinkit.application.productAndCategory.repositories.DarkStoreRepository;
import com.blinkit.application.productAndCategory.repositories.InventoryRepository;
import com.blinkit.application.productAndCategory.repositories.ProductRepository;
import com.blinkit.application.userAuth.entities.User;
import com.blinkit.application.userAuth.exceptions.UserNotFoundException;
import com.blinkit.application.userAuth.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private DarkStoreRepository darkStoreRepository;
    @Autowired private CartService cartService;
    @Autowired private UserRepository userRepository;

    @Transactional
    public OrderResponse placeOrder(Long userId, PlaceOrderRequest request) {
        CartResponse cart = cartService.getCart(userId);

        if (cart.items().isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }

        // Reserve stock for every item FIRST — if any fails, the whole transaction rolls back,
        // so we never end up with an order that's missing an item's stock
        for (CartItemResponse item : cart.items()) {
            int updated = inventoryRepository.decrementStock(item.productId(), cart.storeId(), item.quantity());
            if (updated == 0) {
                throw new InsufficientStockException("Item went out of stock: " + item.name());
            }
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        DarkStore store = darkStoreRepository.findById(cart.storeId())
                .orElseThrow(() -> new DarkStoreNotFoundException("Store not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStore(store);
        order.setSubtotal(cart.subtotal());
        order.setDeliveryFee(cart.deliveryFee());
        order.setTotalAmount(cart.total());
        order.setDeliveryAddress(request.deliveryAddress());
        order.setStatus(OrderStatus.PLACED);

        for (CartItemResponse item : cart.items()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(productRepository.getReferenceById(item.productId()));
            orderItem.setQuantity(item.quantity());
            orderItem.setPriceAtOrder(item.sellingPrice());
            order.getItems().add(orderItem);
        }

        order = orderRepository.save(order);   // cascades and saves OrderItems too
        cartService.clearCart(userId);          // cart is only cleared AFTER order is safely persisted

        return toResponse(order);
    }

    public List<OrderSummaryResponse> getOrderHistory(Long userId) {
        return orderRepository.findByUserIdOrderByPlacedAtDesc(userId).stream()
                .map(o -> new OrderSummaryResponse(o.getId(), o.getStatus().name(), o.getTotalAmount(), o.getItems().size(), o.getPlacedAt()))
                .toList();
    }

    public OrderResponse getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getProduct().getId(), i.getProduct().getName(), i.getProduct().getImage(),
                        i.getQuantity(), i.getPriceAtOrder(), i.getPriceAtOrder() * i.getQuantity()
                )).toList();

        return new OrderResponse(
                order.getId(), order.getStatus().name(), items,
                order.getSubtotal(), order.getDeliveryFee(), order.getTotalAmount(),
                order.getDeliveryAddress(), order.getPlacedAt()
        );
    }

    public List<AdminOrderResponse> getAllOrdersForAdmin() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "placedAt")).stream()
                .map(o -> new AdminOrderResponse(
                        o.getId(), o.getUser().getName(), o.getUser().getEmail(),
                        o.getStatus().name(), o.getTotalAmount(), o.getDeliveryAddress(), o.getPlacedAt()
                ))
                .toList();
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

}