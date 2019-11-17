package com.example.shop.dto;

import com.example.shop.entities.OrderEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class OrderDto {

    public OrderDto() {
    }

    public OrderDto(Long id, BigDecimal total, OffsetDateTime dateCreated, List<OrderPositionDto> positions) {
        this.id = id;
        this.total = total;
        this.dateCreated = dateCreated;
        this.positions = positions;
    }

    private Long id;
    private BigDecimal total;
    private OffsetDateTime dateCreated;
    private List<OrderPositionDto> positions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public static OrderDto fromEntity(OrderEntity entity) {
        return new OrderDto(entity.getId(), entity.getTotal(), entity.getDateCreated(),
                entity.getPositions().stream().map(OrderPositionDto::fromEntity).collect(Collectors.toList()));
    }

    public List<OrderPositionDto> getPositions() {
        return positions;
    }

    public void setPositions(List<OrderPositionDto> positions) {
        this.positions = positions;
    }
}
