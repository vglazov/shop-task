package com.example.shop.controllers;

import com.example.shop.dao.OrderRepository;
import com.example.shop.dao.ProductRepository;
import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderPositionDto;
import com.example.shop.entities.OrderEntity;
import com.example.shop.entities.OrderPositionEntity;
import com.example.shop.entities.ProductEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(path = "/orders")
public class OrderController {

    private OrderRepository orderRepository;

    private ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @RequestMapping(method = POST)
    public ResponseEntity<OrderDto> add(@RequestBody OrderDto order) {
        OrderEntity entity = new OrderEntity();
        for(OrderPositionDto position: order.getPositions()) {
            ProductEntity product = productRepository.findById(position.getProductId()).orElseThrow();
            entity.getPositions().add(new OrderPositionEntity(product.getPrice(), product, position.getQuantity()));
        }
        entity.setDateCreated(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        entity.setTotal(calculateTotal(entity));

        OrderEntity result = orderRepository.save(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(OrderDto.fromEntity(result));
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<OrderDto>> retrieve(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        List<OrderDto> orders = orderRepository.findByDateCreatedBetween(from, to)
                .stream().map(OrderDto::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    // TODO move business logic to separate layer
    private BigDecimal calculateTotal(OrderEntity order) {
        BigDecimal total = BigDecimal.ZERO;
        for(OrderPositionEntity position: order.getPositions()) {
            total = total.add(position.getPrice().multiply(BigDecimal.valueOf(position.getQuantity())));
        }
        return total;
    }
}
