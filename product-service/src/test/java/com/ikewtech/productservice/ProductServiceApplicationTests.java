package com.ikewtech.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikewtech.productservice.dto.ProductRequest;
import com.ikewtech.productservice.dto.ProductResponse;
import com.ikewtech.productservice.mapper.ProductResponseMapper;
import com.ikewtech.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(
            "mongo:4.4.2"
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseMapper productResponseMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();

        String productRequestString = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(status().isOk()).andReturn();

        List<ProductResponse> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

        List<ProductResponse> expected = productRepository.findAll().stream().map(productResponseMapper).toList();

        Assertions.assertEquals(actual, expected);
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("Iphone 15")
                .description("amazing Iphone 15")
                .price(BigDecimal.valueOf(1500))
                .build();
    }


}
