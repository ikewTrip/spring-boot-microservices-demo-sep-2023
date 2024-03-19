package com.ikewtech.productservice.service;

import com.ikewtech.productservice.dto.ProductRequest;
import com.ikewtech.productservice.dto.ProductResponse;
import com.ikewtech.productservice.mapper.ProductResponseMapper;
import com.ikewtech.productservice.model.Product;
import com.ikewtech.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductResponseMapper productResponseMapper;

    public ProductService(ProductRepository productRepository, ProductResponseMapper productResponseMapper) {
        this.productRepository = productRepository;
        this.productResponseMapper = productResponseMapper;
    }

    @Transactional
    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
       List<Product> products = productRepository.findAll();

       return products.stream().map(productResponseMapper).collect(Collectors.toList());
    }
}
