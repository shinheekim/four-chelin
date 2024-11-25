package com.example.fourchelin.search.service;

import com.example.fourchelin.domain.search.service.SearchService;
import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.exception.StoreException;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchStore_Success() {

        String keyword = "카페";
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);

        Store store1 = new Store(1L, "아카페라", "서울시 강남구");
        Store store2 = new Store(2L, "카페라떼는말이야", "서울시 종로구");
        Store store3 = new Store(3L, "레스토랑", "서울시 서초구");
        List<Store> allStores = List.of(store1, store2, store3);

        // 가게 목록을 키워드에 따라 필터링하여 반환하도록 설정
        when(storeRepository.findByKeyword(eq(keyword), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    // 검색어를 기준으로 가게 목록을 필터링
                    List<Store> filteredStores = allStores.stream()
                            .filter(store -> store.getStoreName().contains(keyword))
                            .toList();
                    return new PageImpl<>(filteredStores, pageable, filteredStores.size());
                });

        StorePageResponse response = searchService.searchStore(keyword, page, size);

        assertThat(response).isNotNull();
        assertThat(response.getStoreResponses()).hasSize(2);
        assertThat(response.getTotalElements()).isEqualTo(2);
    }

    @Test
    void searchStore_Success_EmptySearchData() {
        String keyword = "햄버거";
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);

        Store store1 = new Store(1L, "아카페라", "서울시 강남구");
        Store store2 = new Store(2L, "카페라떼는말이야", "서울시 종로구");
        Store store3 = new Store(3L, "레스토랑", "서울시 서초구");
        List<Store> allStores = List.of(store1, store2, store3);

        // 가게 목록을 키워드에 따라 필터링하여 반환하도록 설정
        when(storeRepository.findByKeyword(eq(keyword), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    // 검색어를 기준으로 가게 목록을 필터링
                    List<Store> filteredStores = allStores.stream()
                            .filter(store -> store.getStoreName().contains(keyword))
                            .toList();
                    return new PageImpl<>(filteredStores, pageable, filteredStores.size());
                });

        StorePageResponse response = searchService.searchStore(keyword, page, size);

        assertThat(response).isNotNull();
        assertThat(response.getStoreResponses()).isEmpty();
        assertThat(response.getTotalElements()).isEqualTo(0);

    }

    @Test
    void searchService_Fail_NoKeyword() {
        String keyword = "";
        int page = 1;
        int size = 10;

        assertThatThrownBy(() -> searchService.searchStore(keyword, page, size))
                .isInstanceOf(StoreException.class)
                .hasMessage("검색어를 입력해주세요");
    }

}
