package com.example.fourchelin.domain.search.controller;

import com.example.fourchelin.common.template.RspTemplate;
import com.example.fourchelin.domain.search.service.SearchService;
import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/searches")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/v1/stores")
    public RspTemplate<StorePageResponse> searchStore(@RequestParam String keyword,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        StorePageResponse res = searchService.searchStore(keyword, page, size);
        return new RspTemplate<>(HttpStatus.OK, res);

    }
}
