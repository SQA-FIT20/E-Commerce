package com.example.ecommerce.dto.request.promotion;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreatePromotionRequest {

    private String name;
    private double percent;
    private String description;
    private Long storeId;
    private int quantity;
    private LocalDateTime startAt;
    private LocalDateTime expiredAt;
}
