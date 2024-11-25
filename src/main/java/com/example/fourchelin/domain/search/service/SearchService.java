package com.example.fourchelin.domain.search.service;

import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;

    public StorePageResponse searchStore(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Store> stores = storeRepository.findByKeyword(keyword, pageable);

        return new StorePageResponse(stores);

    }
}
