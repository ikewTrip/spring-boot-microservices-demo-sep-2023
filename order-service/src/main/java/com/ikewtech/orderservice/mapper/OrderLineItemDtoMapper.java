package com.ikewtech.orderservice.mapper;

import com.ikewtech.orderservice.dto.OrderLineItemDto;
import com.ikewtech.orderservice.model.OrderLineItem;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OrderLineItemDtoMapper implements Function<OrderLineItemDto, OrderLineItem> {
    @Override
    public OrderLineItem apply(OrderLineItemDto orderLineItemDto) {

        OrderLineItem orderLineItem = new OrderLineItem();

        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());

        return orderLineItem;
    }
}
