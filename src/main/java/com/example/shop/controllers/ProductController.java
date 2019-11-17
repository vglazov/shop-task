package com.example.shop.controllers;

import com.example.shop.dao.ProductRepository;
import com.example.shop.dto.ProductDto;
import com.example.shop.entities.ProductEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping(path = "/products")
public class ProductController {

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(method = GET)
    public ResponseEntity<Stream<ProductDto>> getAll() {
        Stream<ProductDto> products = StreamSupport.stream(this.productRepository.findAll().spliterator(), false)
                .map(ProductDto::fromEntity);
        return ResponseEntity.ok(products);
    }

    @RequestMapping(method = PUT, path = "/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto product) {
        Optional<ProductEntity> entityOptional = productRepository.findById(id);
        if(entityOptional.isPresent()) {
            ProductEntity entity = entityOptional.get();
            entity.setName(product.getName());
            entity.setPrice(product.getPrice());
            entity = productRepository.save(entity);
            return ResponseEntity.ok(ProductDto.fromEntity(entity));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = POST)
    public ResponseEntity<ProductDto> add(@RequestBody ProductDto product) {
        ProductEntity result = productRepository.save(new ProductEntity(product.getName(), product.getPrice()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductDto.fromEntity(result));
    }
}
