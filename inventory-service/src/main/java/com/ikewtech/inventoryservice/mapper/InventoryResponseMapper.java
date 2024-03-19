package com.ikewtech.inventoryservice.mapper;

import com.ikewtech.inventoryservice.dto.InventoryResponse;
import com.ikewtech.inventoryservice.model.Inventory;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class InventoryResponseMapper implements Function<Inventory, InventoryResponse> {
    @Override
    public InventoryResponse apply(Inventory inventory) {
        return new InventoryResponse(inventory.getSkuCode(), inventory.getQuantity() > 0);
    }
}
