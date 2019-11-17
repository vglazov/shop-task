package com.example.shop;

import com.example.shop.dao.OrderRepository;
import com.example.shop.dao.ProductRepository;
import com.example.shop.entities.OrderEntity;
import com.example.shop.entities.OrderPositionEntity;
import com.example.shop.entities.ProductEntity;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

public abstract class BaseTests {

    private RequestSpecification spec;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation).operationPreprocessors()
                .withRequestDefaults(prettyPrint()).withResponseDefaults(prettyPrint()))
                .build();
    }

    @BeforeEach
    void clear() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    RequestSpecification given() {
        return RestAssured.given(this.spec);
    }

    ProductEntity addSampleProduct() {
       return productRepository.save(new ProductEntity("Sample", BigDecimal.valueOf(9.99).setScale(2, RoundingMode.DOWN)));
    }

    OrderEntity addSampleOrder() {
        ProductEntity product = addSampleProduct();
        OrderPositionEntity position = new OrderPositionEntity(BigDecimal.valueOf(5), product, 3);
        OrderEntity order = new OrderEntity(BigDecimal.valueOf(15), OffsetDateTime.now());
        order.getPositions().add(position);
        return orderRepository.save(order);
    }

    BigDecimal randomPrice() {
        return new BigDecimal(Math.random() * 1000d).setScale(2, RoundingMode.DOWN);
    }


}
