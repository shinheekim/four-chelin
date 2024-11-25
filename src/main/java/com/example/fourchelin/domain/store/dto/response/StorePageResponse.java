package com.example.fourchelin.domain.store.dto.response;

import com.example.fourchelin.domain.store.entity.Store;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class StorePageResponse {

    private List<StoreResponse> storeResponses;
    private int totalPages;
    private long totalElements;

    public StorePageResponse(Page<Store> stores) {
        this.storeResponses = stores.map(StoreResponse::to).stream().toList();
        this.totalPages = stores.getTotalPages();
        this.totalElements = stores.getTotalElements();
    }
}
