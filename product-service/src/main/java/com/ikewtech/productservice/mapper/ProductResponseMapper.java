package com.ikewtech.productservice.mapper;

import com.ikewtech.productservice.dto.ProductRequest;
import com.ikewtech.productservice.dto.ProductResponse;
import com.ikewtech.productservice.model.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductResponseMapper implements Function<Product, ProductResponse> {
    @Override
    public ProductResponse apply(Product productRequest) {
        return new ProductResponse(
                productRequest.getId(),
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice()
        );
    }
}
