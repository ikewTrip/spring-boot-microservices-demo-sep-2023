package com.ikewtech.inventoryservice.service;

import com.ikewtech.inventoryservice.dto.InventoryResponse;
import com.ikewtech.inventoryservice.mapper.InventoryResponseMapper;
import com.ikewtech.inventoryservice.reposetory.InventoryRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryResponseMapper inventoryResponseMapper;

    public InventoryService(InventoryRepository inventoryRepository, InventoryResponseMapper inventoryResponseMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryResponseMapper = inventoryResponseMapper;
    }

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventoryResponseMapper)
                .toList();
    }
}

