package com.example.shop;

import com.example.shop.dto.ProductDto;
import com.example.shop.entities.ProductEntity;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.math.BigDecimal;
import java.util.Optional;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = DEFINED_PORT)
@DisplayName("/products endpoint")
class ProductTests extends BaseTests {

    private final FieldDescriptor NAME_DESCRIPTION = fieldWithPath("name").description("Product name");
    private final FieldDescriptor PRICE_DESCRIPTION = fieldWithPath("price").description("Product price");

    @Test
    @DisplayName("Add product")
    void add() {
        Response response = given()
                .filter(document("add-product", requestFields(NAME_DESCRIPTION, PRICE_DESCRIPTION)))
                .with().contentType("application/json")
                .body(new ProductDto("test", new BigDecimal(67)))
                .post("products").thenReturn();

        response.then().statusCode(CREATED.value());
        Long id = response.as(ProductDto.class).getId();

        assertNotNull(id, "Id of created product is returned");
        assertEquals(1, productRepository.count(), "Product is created in database");
        assertTrue(productRepository.findById(id).isPresent(), "Product can be retrieved by id");
    }

    @Test
    @DisplayName("Get all products")
    void getAll() {
        ProductEntity sample = addSampleProduct();
        given().filter(document("get-products"))
                .get("products")
                .then()
                .statusCode(OK.value()).contentType(JSON)
                .body("$", hasSize(1))
                .body("[0].name", is(sample.getName()));
    }

    @Test
    @DisplayName("Update product")
    void put() {
        Long id = addSampleProduct().getId();
        BigDecimal newPrice = randomPrice();
        String newName = "Updated name";
        given().filter(document("update-product", requestFields(NAME_DESCRIPTION, PRICE_DESCRIPTION)))
                .contentType("application/json")
                .body(new ProductDto(newName, newPrice))
                .put("products/" + id)
                .then()
                .statusCode(OK.value()).contentType(JSON)
                .body("name", is(newName));

        Optional<ProductEntity> updatedProduct = productRepository.findById(id);
        assertTrue(updatedProduct.isPresent(), "Product is still there");
        assertEquals(newName, updatedProduct.get().getName(), "Name is updated");
        assertEquals(newPrice, updatedProduct.get().getPrice(), "Price is updated");
    }

}


