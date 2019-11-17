package com.example.shop.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "order_positions")
public class OrderPositionEntity {

    public OrderPositionEntity(BigDecimal price, ProductEntity product, Integer quantity) {
        this.price = price;
        this.product = product;
        this.quantity = quantity;
    }

    public OrderPositionEntity() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
