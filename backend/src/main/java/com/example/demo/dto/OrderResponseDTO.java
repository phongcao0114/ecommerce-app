package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Data
public class OrderResponseDTO {
    private Long id;
    private String userEmail;
    private LocalDateTime date;
    private String status;
    private String paymentMethod;
    private List<OrderItemDTO> items;
    private String shippingAddress;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingCountry;
    private String phoneNumber;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
}
