package com.example.demo.service;

import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.dto.PlaceOrderRequestDTO;
import com.example.demo.dto.PlaceOrderResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Transactional
    public Order placeOrder(PlaceOrderRequestDTO request) {
        // 1. Get the current user's email from the security context
        String userEmail = getCurrentUserEmail();
        // 2. Retrieve the user entity from the database
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // 3. Get the list of selected product IDs from the request
        List<Long> selectedProductIds = request.getProductIds();
        // 4. Validate that at least one product is selected
        if (selectedProductIds == null || selectedProductIds.isEmpty()) {
            throw new IllegalArgumentException("No products selected for order");
        }
        // 5. Retrieve cart items for the user that match the selected product IDs
        List<CartItem> cartItems = cartItemRepository.findByUserEmail(userEmail)
                .stream()
                .filter(item -> selectedProductIds.contains(item.getProduct().getId()))
                .toList();
        // 6. Validate that matching cart items exist
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("No matching cart items found for selected products");
        }
        // 7. Create a new Order entity and set its properties
        Order order = new Order();
        order.setUser(user);
        order.setDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingAddress(request.getShippingAddress());
        order.setShippingCity(request.getShippingCity());
        order.setShippingPostalCode(request.getShippingPostalCode());
        order.setShippingCountry(request.getShippingCountry());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setShippingFee(request.getShippingFee());
        order.setTotalAmount(request.getTotalAmount());
        // 8. Save the order to generate its ID
        order = orderRepository.save(order);
        // 9. For each cart item, check stock, update product stock, and create an OrderItem
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            // 9a. Check if there is enough stock for the product
            if (cartItem.getQuantity() > product.getStock()) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }
            // 9b. Deduct the ordered quantity from the product's stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
            // 9c. Create and save an OrderItem for this product
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItemRepository.save(orderItem);
        }
        // 10. Remove the processed cart items from the user's cart
        cartItemRepository.deleteAllByIdInBatch(cartItems.stream().map(CartItem::getId).toList());
        // 11. Return the created order
        return order;
    }

    @Transactional
    public PlaceOrderResponseDTO placeOrderWithResponse(PlaceOrderRequestDTO request) {
        Order order = placeOrder(request);
        return new PlaceOrderResponseDTO(order.getId(), "Order placed successfully");
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserId(Long userId) {
        User user = userRepository.findById(userId.intValue()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return orderRepository.findByUser(user);
    }

    public List<OrderResponseDTO> getAllOrdersSafe() {
        return orderRepository.findAll().stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(order.getId());
            dto.setUserEmail(order.getUser().getEmail());
            dto.setDate(order.getDate());
            dto.setStatus(order.getStatus());
            dto.setPaymentMethod(order.getPaymentMethod());
            dto.setShippingAddress(order.getShippingAddress());
            dto.setShippingCity(order.getShippingCity());
            dto.setShippingPostalCode(order.getShippingPostalCode());
            dto.setShippingCountry(order.getShippingCountry());
            dto.setPhoneNumber(order.getPhoneNumber());
            dto.setShippingFee(order.getShippingFee());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setItems(order.getItems().stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setProductId(item.getProduct().getId());
                itemDTO.setProductName(item.getProduct().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setProductImage(item.getProduct().getImageUrl());
                return itemDTO;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getOrdersByUserIdSafe(Long userId) {
        User user = userRepository.findById(userId.intValue()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return orderRepository.findByUser(user).stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(order.getId());
            dto.setUserEmail(order.getUser().getEmail());
            dto.setDate(order.getDate());
            dto.setStatus(order.getStatus());
            dto.setPaymentMethod(order.getPaymentMethod());
            dto.setShippingAddress(order.getShippingAddress());
            dto.setShippingCity(order.getShippingCity());
            dto.setShippingPostalCode(order.getShippingPostalCode());
            dto.setShippingCountry(order.getShippingCountry());
            dto.setPhoneNumber(order.getPhoneNumber());
            dto.setShippingFee(order.getShippingFee());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setItems(order.getItems().stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setProductId(item.getProduct().getId());
                itemDTO.setProductName(item.getProduct().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setProductImage(item.getProduct().getImageUrl());
                return itemDTO;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getOrdersByStatusSafe(String status) {
        return orderRepository.findByStatus(status).stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(order.getId());
            dto.setUserEmail(order.getUser().getEmail());
            dto.setDate(order.getDate());
            dto.setStatus(order.getStatus());
            dto.setPaymentMethod(order.getPaymentMethod());
            dto.setShippingAddress(order.getShippingAddress());
            dto.setShippingCity(order.getShippingCity());
            dto.setShippingPostalCode(order.getShippingPostalCode());
            dto.setShippingCountry(order.getShippingCountry());
            dto.setPhoneNumber(order.getPhoneNumber());
            dto.setShippingFee(order.getShippingFee());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setItems(order.getItems().stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setProductId(item.getProduct().getId());
                itemDTO.setProductName(item.getProduct().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setProductImage(item.getProduct().getImageUrl());
                return itemDTO;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getCurrentUserOrdersSafe() {
        String userEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return orderRepository.findByUser(user).stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(order.getId());
            dto.setUserEmail(order.getUser().getEmail());
            dto.setDate(order.getDate());
            dto.setStatus(order.getStatus());
            dto.setPaymentMethod(order.getPaymentMethod());
            dto.setShippingAddress(order.getShippingAddress());
            dto.setShippingCity(order.getShippingCity());
            dto.setShippingPostalCode(order.getShippingPostalCode());
            dto.setShippingCountry(order.getShippingCountry());
            dto.setPhoneNumber(order.getPhoneNumber());
            dto.setShippingFee(order.getShippingFee());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setItems(order.getItems().stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setProductId(item.getProduct().getId());
                itemDTO.setProductName(item.getProduct().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setProductImage(item.getProduct().getImageUrl());
                return itemDTO;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public boolean isOrderOwner(Long orderId, String email) {
        return orderRepository.findById(orderId)
                .map(order -> order.getUser().getEmail().equals(email))
                .orElse(false);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    public void markOrderDeliveredByUser(Long orderId) {
        String userEmail = getCurrentUserEmail();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("You can only mark your own order as delivered");
        }
        order.setStatus("DELIVERED");
        orderRepository.save(order);
    }
}
