package com.example.fourchelin.domain.search.service;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.search.entity.SearchHistory;
import com.example.fourchelin.domain.search.repository.SearchHistoryRepository;
import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.exception.StoreException;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    public StorePageResponse searchStore(String keyword, int page, int size, Member member) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new StoreException("검색어를 입력해주세요");
        }

        // 인증된 사용자인 경우에만 검색기록 저장
        if (member != null) {
            saveOrUpdateSearchHistory(keyword, member);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Store> stores = storeRepository.findByKeyword(keyword, pageable);
        return new StorePageResponse(stores);
    }

    public List<String> searchKeywordStore(Member member) {
        // 인증 유저가 아닌 경우
        if (member == null) {
            return List.of();
        }
        List<SearchHistory> searchHistories = searchHistoryRepository.keywordFindByMember(member);
        // 검색 결과가 없는 경우
        if (searchHistories.isEmpty()) {
            return List.of();
        }
        return searchHistories.stream()
                .map(SearchHistory::getKeyword)
                .toList();
    }

    private void saveOrUpdateSearchHistory(String keyword, Member member) {
        // 동일한 키워드가 존재한다면 searchDateTime만 update
        SearchHistory existKeyword = searchHistoryRepository.findByKeywordAndMember(keyword, member);
        if (existKeyword != null) {
            existKeyword.updateSearchDateTime(LocalDateTime.now());
        } else {
            SearchHistory searchHistory = SearchHistory.builder()
                    .keyword(keyword)
                    .searchDateTime(LocalDateTime.now())
                    .member(member)
                    .build();
            searchHistoryRepository.save(searchHistory);
        }
    }
}