package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private LocalDateTime date;
    @Column(nullable = false, length = 50)
    private String status;
    @Column(nullable = false, length = 30)
    private String paymentMethod; // e.g., COD, CreditCard, InternetBanking
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items;
    @Column(nullable = false, length = 255)
    private String shippingAddress;
    @Column(nullable = false, length = 100)
    private String shippingCity;
    @Column(nullable = false, length = 20)
    private String shippingPostalCode;
    @Column(nullable = false, length = 100)
    private String shippingCountry;
    @Column(nullable = false, length = 20)
    private String phoneNumber;
    @Column(nullable = false)
    private BigDecimal shippingFee;
    @Column(nullable = false)
    private BigDecimal totalAmount;
}
