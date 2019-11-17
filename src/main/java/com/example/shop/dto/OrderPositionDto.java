package com.example.shop.dto;

import com.example.shop.entities.OrderPositionEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class OrderPositionDto {

    public OrderPositionDto(Integer quantity, Long productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public OrderPositionDto(BigDecimal price, Integer quantity, Long productId) {
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
    }

    public OrderPositionDto() {
    }

    private BigDecimal price;
    private Integer quantity;
    private Long productId;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public static OrderPositionDto fromEntity(OrderPositionEntity entity) {
        return new OrderPositionDto(entity.getPrice(), entity.getQuantity(), entity.getProduct().getId());
    }
}
