package com.example.fourchelin.domain.store.service;

import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public StorePageResponse findStores(String keyword, String category, Integer star, int page, Integer size, String criteria) {

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc(criteria)));

        Page<Store> stores = storeRepository.findByConditionStores(keyword, category, star, pageable);

        return new StorePageResponse(stores);

    }
}
