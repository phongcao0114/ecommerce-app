package com.example.demo.service;

import com.example.demo.dto.CartItemRequestDTO;
import com.example.demo.dto.CartItemResponseDTO;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    private void validateStock(Product product, int quantity) {
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock.");
        }
    }

    @Transactional
    public void addItemToCart(CartItemRequestDTO request) {
        String userEmail = getCurrentUserEmail();
        Product product = productRepository.findById(request.getProductId()).orElseThrow();
        Optional<CartItem> existing = cartItemRepository.findByUserEmailAndProductId(userEmail, product.getId());
        int requestedQuantity = request.getQuantity();
        int currentQuantity = existing.map(CartItem::getQuantity).orElse(0);
        int totalQuantity = currentQuantity + requestedQuantity;
        validateStock(product, totalQuantity);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(totalQuantity);
            cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setUserEmail(userEmail);
            item.setProduct(product);
            item.setQuantity(requestedQuantity);
            cartItemRepository.save(item);
        }
    }

    public List<CartItemResponseDTO> getCartItems() {
        String userEmail = getCurrentUserEmail();
        List<CartItem> items = cartItemRepository.findByUserEmail(userEmail);
        return items.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void updateCartItem(Long productId, int quantity) {
        String userEmail = getCurrentUserEmail();
        CartItem item = cartItemRepository.findByUserEmailAndProductId(userEmail, productId).orElseThrow();
        Product product = item.getProduct();
        validateStock(product, quantity);
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    @Transactional
    public void updateCartItemByEmailAndProductId(Long productId, int quantity) {
        String userEmail = getCurrentUserEmail();
        CartItem item = cartItemRepository.findByUserEmailAndProductId(userEmail, productId).orElseThrow();
        Product product = item.getProduct();
        validateStock(product, quantity);
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    @Transactional
    public void removeCartItem(Long productId) {
        String userEmail = getCurrentUserEmail();
        // Check if the product exists before attempting to delete from cart
        productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found."));
        cartItemRepository.deleteByUserEmailAndProductId(userEmail, productId);
    }

    @Transactional
    public void removeAllCartItemsForCurrentUser() {
        String userEmail = getCurrentUserEmail();
        cartItemRepository.deleteAllByUserEmail(userEmail);
    }

    @Transactional
    public void deleteCartItemsByIds(List<Long> cartItemIds) {
        if (cartItemIds != null && !cartItemIds.isEmpty()) {
            cartItemRepository.deleteAllByIdInBatch(cartItemIds);
        }
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private CartItemResponseDTO toDTO(CartItem item) {
        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setId(item.getId());
        Product product = item.getProduct();
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductImage(product.getImageUrl());
        dto.setProductPrice(product.getPrice() != null ? product.getPrice().doubleValue() : 0.0);
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}
