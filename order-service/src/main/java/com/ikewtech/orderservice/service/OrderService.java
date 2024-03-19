package com.ikewtech.orderservice.service;

import com.ikewtech.orderservice.dto.InventoryResponse;
import com.ikewtech.orderservice.dto.OrderRequest;
import com.ikewtech.orderservice.event.OrderPlacedEvent;
import com.ikewtech.orderservice.mapper.OrderLineItemDtoMapper;
import com.ikewtech.orderservice.model.Order;
import com.ikewtech.orderservice.model.OrderLineItem;
import com.ikewtech.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderLineItemDtoMapper orderLineItemDtoMapper;
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Transactional
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> list = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemDtoMapper)
                .toList();
        order.setOrderLineItemsList(list);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItem::getSkuCode).toList();

        // Call Inventory Service, and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

        if (allProductsInStock){
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order Placed Successfully";
        } else {
            throw new IllegalArgumentException("Product is out of stock, please try again later");
        }

    }
}
