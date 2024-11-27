package com.example.fourchelin.domain.store.repository.support;

import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.enums.StoreCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {

    Page<Store> findByConditionStores(String keyword, String category, Integer star, Pageable pageable);

    Page<Store> findByfilters(String keyword, Pageable pageable, int star, String status);
}
