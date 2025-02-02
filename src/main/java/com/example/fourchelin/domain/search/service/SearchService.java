package com.example.fourchelin.domain.search.service;

import com.example.fourchelin.common.service.CacheService;
import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.search.entity.PopularKeyword;
import com.example.fourchelin.domain.search.entity.SearchHistory;
import com.example.fourchelin.domain.search.repository.PopularKeywordRepository;
import com.example.fourchelin.domain.search.repository.SearchHistoryRepository;
import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.exception.StoreException;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final PopularKeywordRepository popularKeywordRepository;
    private final CacheService cacheService1;
    private final CacheService cacheService2;

    @Transactional
    public StorePageResponse searchStore(String keyword, int page, int size, Member member) {
        searchKeywordAndUpdateSearchHistory(keyword, member);
        saveOrUpdatePopularKeyword(keyword);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Store> stores = storeRepository.findByKeyword(keyword, pageable);
        return new StorePageResponse(stores);
    }

    @Transactional
    public StorePageResponse searchStoreV2(String keyword, int page, int size, Member member) {
        searchKeywordAndUpdateSearchHistory(keyword, member);
        cacheService1.saveOrUpdateKeywordCount(keyword);
        cacheService1.displayCache("keywordCounts");
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Store> stores = storeRepository.findByKeyword(keyword, pageable);
        return new StorePageResponse(stores);
    }

    public void searchKeywordAndUpdateSearchHistory(String keyword, Member member) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new StoreException("검색어를 입력해주세요");
        }
        if (member != null) {
            saveOrUpdateSearchHistory(keyword, member);
        }
    }

    public Map<String, List<String>> searchKeyword(Member member) {
        Map<String, List<String>> result = new HashMap<>();
        if (member != null) {
            List<SearchHistory> searchHistories = searchHistoryRepository.keywordFindByMember(member);
            List<String> searchKeywords = searchHistories.stream()
                    .map(SearchHistory::getKeyword)
                    .toList();
            result.put("userSearchHistory", searchKeywords);
        }
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        List<String> popularKeywords = popularKeywordRepository.findTop10PopularKeywords(sevenDaysAgo);
        result.put("popularKeywords", popularKeywords);
        return result;
    }

    public Map<String, List<String>> searchKeywordV2(Member member) {
        Map<String, List<String>> result = new HashMap<>();
        if (member != null) {
            List<SearchHistory> searchHistories = searchHistoryRepository.keywordFindByMember(member);
            List<String> searchKeywords = searchHistories.stream()
                    .map(SearchHistory::getKeyword)
                    .toList();
            result.put("userSearchHistory", searchKeywords);
        } else {
            result.put("userSearchHistory", List.of());
        }
        Map<String, Long> top10Keywords = cacheService2.getAllKeywordCountsFromCache2();
        List<String> topKeywords = top10Keywords.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .toList();
        result.put("top10Keywords", topKeywords);
        return result;
    }

    private void saveOrUpdateSearchHistory(String keyword, Member member) {
        SearchHistory existKeyword = searchHistoryRepository.findByKeywordAndMember(keyword, member);
        if (existKeyword != null) {
            existKeyword.updateSearchDateTime(LocalDateTime.now());
            searchHistoryRepository.save(existKeyword);
        } else {
            SearchHistory searchHistory = SearchHistory.builder()
                    .keyword(keyword)
                    .searchDateTime(LocalDateTime.now())
                    .member(member)
                    .build();
            searchHistoryRepository.save(searchHistory);
        }
    }

    private void saveOrUpdatePopularKeyword(String keyword) {
        LocalDate today = LocalDate.now();
        Optional<PopularKeyword> optionalPopularKeyword = popularKeywordRepository.findByKeywordAndTrendDate(keyword, today);

        if (optionalPopularKeyword.isPresent()) {
            PopularKeyword popularKeyword = optionalPopularKeyword.get();
            popularKeyword.incrementCount();
            popularKeywordRepository.save(popularKeyword);
        } else {
            PopularKeyword newPopularKeyword = new PopularKeyword(keyword, today);
            popularKeywordRepository.save(newPopularKeyword);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    @Transactional
    public void processCacheAndUpdateDB() {
        Map<String, Long> cache1Data = cacheService1.getAllKeywordCountsFromCache1();
        LocalDate today = LocalDate.now();
        cache1Data.forEach((keyword, count) -> {
            popularKeywordRepository.saveOrUpdateKeyword(keyword, count, today);
        });
        Map<String, Long> top10Keywords = popularKeywordRepository.findTop10KeywordsForLast7Days();
        cacheService1.clearKeywordCountsCache();
        cacheService2.clearTop10KeywordsCache();
        cacheService2.saveAllKeywordsToCache(top10Keywords);
    }
}