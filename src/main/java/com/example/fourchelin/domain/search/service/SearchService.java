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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final PopularKeywordRepository popularKeywordRepository;
    private final CacheManager cacheManager;
    private final CacheService cacheService;
    private final KeywordCacheService keywordCacheService;

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
        keywordCacheService.saveOrUpdateKeywordCountWithCache(keyword, LocalDate.now());
        cacheService.displayCache("keywordCounts");
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
        } else {
            result.put("userSearchHistory", List.of());
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
        Map<String, Long> dbKeywordCounts = popularKeywordRepository.findKeywordCountsForLast7Days();
        Map<String, Long> cacheKeywordCounts = getAllKeywordCountsFromCache();
        Map<String, Long> combinedKeywordCounts = new HashMap<>(dbKeywordCounts);
        if (cacheKeywordCounts != null) {
            cacheKeywordCounts.forEach((keyword, count) ->
                    combinedKeywordCounts.merge(keyword, count, Long::sum)
            );
        }
        List<String> popularKeywords = combinedKeywordCounts.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();
        result.put("popularKeywords", popularKeywords);
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

    public Map<String, Long> getAllKeywordCountsFromCache() {
        Cache cache = cacheManager.getCache("keywordCounts");
        if (cache == null) {
            return Map.of();
        }
        Map<String, Long> allKeywordCounts = new HashMap<>();
        ConcurrentMap<Object, Object> nativeCache = (ConcurrentMap<Object, Object>) cache.getNativeCache();
        nativeCache.forEach((key, value) -> {
            if (key instanceof String && value instanceof Long) {
                allKeywordCounts.put((String) key, (Long) value);
            }
        });
        return allKeywordCounts;
    }
}