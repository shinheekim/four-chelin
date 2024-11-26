package com.example.fourchelin.search.service;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;
import com.example.fourchelin.domain.search.entity.SearchHistory;
import com.example.fourchelin.domain.search.repository.SearchHistoryRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private SearchHistoryRepository searchHistoryRepository;

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

        Member member = new Member("01012345678", "user1", "password", MemberRole.USER);

        // 가게 목록을 키워드에 따라 필터링하여 반환하도록 설정
        when(storeRepository.findByKeyword(eq(keyword), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    // 검색어를 기준으로 가게 목록을 필터링
                    List<Store> filteredStores = allStores.stream()
                            .filter(store -> store.getStoreName().contains(keyword))
                            .toList();
                    return new PageImpl<>(filteredStores, pageable, filteredStores.size());
                });

        StorePageResponse response = searchService.searchStore(keyword, page, size, member);

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

        Member member = new Member("01012345678", "user1", "password", MemberRole.USER);

        // 가게 목록을 키워드에 따라 필터링하여 반환하도록 설정
        when(storeRepository.findByKeyword(eq(keyword), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    // 검색어를 기준으로 가게 목록을 필터링
                    List<Store> filteredStores = allStores.stream()
                            .filter(store -> store.getStoreName().contains(keyword))
                            .toList();
                    return new PageImpl<>(filteredStores, pageable, filteredStores.size());
                });

        StorePageResponse response = searchService.searchStore(keyword, page, size, member);

        assertThat(response).isNotNull();
        assertThat(response.getStoreResponses()).isEmpty();
        assertThat(response.getTotalElements()).isEqualTo(0);
    }

    @Test
    void searchService_Fail_NoKeyword() {
        String keyword = "";
        int page = 1;
        int size = 10;
        Member member = new Member("01012345678", "user1", "password", MemberRole.USER);

        assertThatThrownBy(() -> searchService.searchStore(keyword, page, size, member))
                .isInstanceOf(StoreException.class)
                .hasMessage("검색어를 입력해주세요");
    }

    // [검색기록 테스트]===========================================================================================

    @Test
    void searchStore_Success_SaveKeword() {

        String keyword = "카페";
        int page = 1;
        int size = 10;
        Member member = new Member("01012345678", "user1", "password", MemberRole.USER);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Store> storePage = new PageImpl<>(List.of(), pageable, 0);

        when(storeRepository.findByKeyword(eq(keyword), any(Pageable.class))).thenReturn(storePage);
        when(searchHistoryRepository.findByKeywordAndMember(eq(keyword), eq(member))).thenReturn(null);

        doAnswer(invocation -> {
            SearchHistory savedHistory = invocation.getArgument(0);
            System.out.println("Saved SearchHistory: " + savedHistory);
            return null;
        }).when(searchHistoryRepository).save(any(SearchHistory.class));

        StorePageResponse response = searchService.searchStore(keyword, page, size, member);

        verify(searchHistoryRepository, times(1)).save(any(SearchHistory.class));
        assertThat(response).isNotNull();
    }

    @Test
    void searchStore_Success_UpdateSearchDateTime() {
        String keyword = "카페";
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);

        // 기존의 검색 기록이 존재하는 상태 설정
        Member member = new Member("01012345678", "user1", "password", MemberRole.USER);
        SearchHistory existingHistory = SearchHistory.builder()
                .keyword(keyword)
                .member(member)
                .searchDateTime(LocalDateTime.now().minusDays(1)) // 하루 전 검색 기록
                .build();

        when(searchHistoryRepository.findByKeywordAndMember(eq(keyword), eq(member)))
                .thenReturn(existingHistory);

        Store store1 = new Store(1L, "아카페라", "서울시 강남구");
        Store store2 = new Store(2L, "카페라떼는말이야", "서울시 종로구");
        List<Store> allStores = List.of(store1, store2);

        // 가게 목록을 키워드에 따라 필터링하여 반환하도록 설정
        when(storeRepository.findByKeyword(eq(keyword), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    List<Store> filteredStores = allStores.stream()
                            .filter(store -> store.getStoreName().contains(keyword))
                            .toList();
                    return new PageImpl<>(filteredStores, pageable, filteredStores.size());
                });

        System.out.println("Before search: searchDateTime = " + existingHistory.getSearchDateTime());
        StorePageResponse response = searchService.searchStore(keyword, page, size, member);
        System.out.println("After search: searchDateTime = " + existingHistory.getSearchDateTime());

        assertThat(response).isNotNull();
        assertThat(response.getStoreResponses()).hasSize(2);
        assertThat(response.getTotalElements()).isEqualTo(2);

        verify(searchHistoryRepository, times(1)).save(existingHistory);
        assertThat(existingHistory.getSearchDateTime()).isAfter(LocalDateTime.now().minusMinutes(1));
    }

    // [검색기록 조회 테스트]===========================================================================================

    @Test
    void searchKeyword_Success() {

        Member member = new Member("01012345678", "user1", "password", MemberRole.USER);

        List<SearchHistory> searchHistories = List.of(
                SearchHistory.builder().keyword("검색어1").member(member).searchDateTime(LocalDateTime.now().minusMinutes(1)).build(),
                SearchHistory.builder().keyword("검색어2").member(member).searchDateTime(LocalDateTime.now().minusMinutes(2)).build(),
                SearchHistory.builder().keyword("검색어3").member(member).searchDateTime(LocalDateTime.now().minusMinutes(3)).build(),
                SearchHistory.builder().keyword("검색어4").member(member).searchDateTime(LocalDateTime.now().minusMinutes(4)).build(),
                SearchHistory.builder().keyword("검색어5").member(member).searchDateTime(LocalDateTime.now().minusMinutes(5)).build(),
                SearchHistory.builder().keyword("검색어6").member(member).searchDateTime(LocalDateTime.now().minusMinutes(6)).build(),
                SearchHistory.builder().keyword("검색어7").member(member).searchDateTime(LocalDateTime.now().minusMinutes(7)).build(),
                SearchHistory.builder().keyword("검색어8").member(member).searchDateTime(LocalDateTime.now().minusMinutes(8)).build(),
                SearchHistory.builder().keyword("검색어9").member(member).searchDateTime(LocalDateTime.now().minusMinutes(9)).build(),
                SearchHistory.builder().keyword("검색어10").member(member).searchDateTime(LocalDateTime.now().minusMinutes(10)).build(),
                SearchHistory.builder().keyword("검색어11").member(member).searchDateTime(LocalDateTime.now().minusMinutes(11)).build(),
                SearchHistory.builder().keyword("검색어12").member(member).searchDateTime(LocalDateTime.now().minusMinutes(12)).build()
        );

        List<SearchHistory> limitedSearchHistories = searchHistories.subList(0, 10);
        when(searchHistoryRepository.keywordFindByMember(any(Member.class))).thenReturn(limitedSearchHistories);


        List<String> keywords = searchService.searchKeywordStore(member);

        System.out.println("검색어 목록: " + keywords);

        assertThat(keywords).isNotNull();
        assertThat(keywords).hasSize(10);
        assertThat(keywords).containsExactly(
                "검색어1", "검색어2", "검색어3", "검색어4", "검색어5",
                "검색어6", "검색어7", "검색어8", "검색어9", "검색어10"
        );
    }

    @Test
    void searchKeyword_Success_NotMember() {
        // 인증되지 않은 사용자
        Member member = null;

        when(searchHistoryRepository.keywordFindByMember(any())).thenReturn(Collections.emptyList());

        List<String> keywords = searchService.searchKeywordStore(member);

        System.out.println("검색어 목록: " + keywords);

        assertThat(keywords).isNotNull();
        assertThat(keywords).isEmpty();
    }

    // []===========================================================================================
}
