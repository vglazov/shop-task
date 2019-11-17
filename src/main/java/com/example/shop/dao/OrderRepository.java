package com.example.shop.dao;

import com.example.shop.entities.OrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    List<OrderEntity> findByDateCreatedBetween(OffsetDateTime from, OffsetDateTime to);
}
