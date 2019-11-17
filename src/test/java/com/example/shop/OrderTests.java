package com.example.shop;

import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderPositionDto;
import com.example.shop.entities.OrderEntity;
import com.example.shop.entities.ProductEntity;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = DEFINED_PORT)
@DisplayName("/orders endpoint")
class OrderTests extends BaseTests {

    @Test
    @DisplayName("Place order")
    void add() {
        ProductEntity product = addSampleProduct();
        int quantity = 2;
        OrderDto order = new OrderDto();
        order.getPositions().add(new OrderPositionDto(quantity, product.getId()));

        Response response = given().filter(document("place-order"))
                .with().contentType("application/json")
                .body(order)
                .post("orders").thenReturn();

        response.then().statusCode(CREATED.value());

        Long id = response.as(OrderDto.class).getId();

        assertNotNull(id, "Id of placed order is returned");
        Optional<OrderEntity> orderOpt = orderRepository.findById(id);
        assertTrue(orderOpt.isPresent(), "Order can be retrieved by id");
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(quantity)),
                orderOpt.get().getTotal(), "Order total is calculated correctly");
        assertEquals(1, orderOpt.get().getPositions().size(), "Order position is created in database");
    }


    @Test
    @DisplayName("Retrieve orders")
    void retrieve() {

        addSampleOrder();

        OffsetDateTime now = OffsetDateTime.now().plusSeconds(1);
        OffsetDateTime hourAgo = now.minusHours(1);
        OffsetDateTime yesterday = now.minusDays(1);

        given().filter(document("retrieve-orders", requestParameters(
                parameterWithName("from").description("'From' date in ISO-8601 extended offset date-time format"),
                parameterWithName("to").description("'To' date in ISO-8601 extended offset date-time format"))))
                .queryParam("from", hourAgo.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .queryParam("to", now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .get("orders")
                .then().statusCode(OK.value()).contentType(JSON)
                .body("$", hasSize(1));

        given().queryParam("from", yesterday.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .queryParam("to", hourAgo.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .get("orders")
                .then().statusCode(OK.value()).contentType(JSON)
                .body("$", hasSize(0));

    }
}


