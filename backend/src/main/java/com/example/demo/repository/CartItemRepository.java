package com.example.demo.repository;

import com.example.demo.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserEmail(String userEmail);

    Optional<CartItem> findByUserEmailAndProductId(String userEmail, Long productId);

    void deleteByUserEmailAndProductId(String userEmail, Long productId);

    void deleteAllByUserEmail(String userEmail);
}
