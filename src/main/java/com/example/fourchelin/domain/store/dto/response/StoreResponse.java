package com.example.fourchelin.domain.store.dto.response;

import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.example.fourchelin.domain.store.enums.StoreStatus;

import java.time.LocalDateTime;

public record StoreResponse(
        Long id,
        String storeName,
        StoreStatus status,
        StoreCategory category,
        String address,
        Integer star
) {
    public static StoreResponse to(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getStoreName(),
                store.getStatus(),
                store.getCategory(),
                store.getAddress(),
                store.getStar()
        );

    }
}
//package com.example.fourchelin.domain.search.dto.response;
//
//import com.example.fourchelin.domain.store.entity.Store;
//
//import java.time.LocalDateTime;
//
//public record SearchResponse(
//        Long id,
//        String storeName,
//        String address,
//        int star,
//        String status,
//        LocalDateTime monitoringDate
//) {
//    public static SearchResponse fromStoreEntity(Store store) {
//        return new SearchResponse(
//                store.getId(),
//                store.getStoreName(),
//                store.getAddress(),
//                store.getStar(),
//                store.getStatus().name(), // enum 상태를 문자열로 변환
//                store.getMonitoringDate()
//        );
//    }
//}
