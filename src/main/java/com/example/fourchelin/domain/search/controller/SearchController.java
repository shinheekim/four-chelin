package com.example.fourchelin.domain.search.controller;

import com.example.fourchelin.common.security.UserDetailsImpl;
import com.example.fourchelin.common.template.RspTemplate;
import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.search.service.SearchService;
import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.dto.response.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/searches")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public RspTemplate<Map<String, List<String>>> searchKeyword(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 인증된 유저일 경우 member 객체 가져오기, 아니면 null
        Member member = (userDetails != null) ? userDetails.getMember() : null;
        Map<String, List<String>> searchResults = searchService.searchKeyword(member);
        return new RspTemplate<>(HttpStatus.OK, searchResults);
    }

    @GetMapping("/v1/stores")
    public RspTemplate<StorePageResponse> searchStore(@RequestParam String keyword,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 인증된 유저일 경우 member 객체 가져오고 아니면 null
        Member member = (userDetails != null) ? userDetails.getMember() : null;
        StorePageResponse res = searchService.searchStore(keyword, page, size, member);
        return new RspTemplate<>(HttpStatus.OK, res);
    }
}
