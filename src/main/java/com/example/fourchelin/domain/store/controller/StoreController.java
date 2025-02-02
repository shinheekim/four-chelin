package com.example.fourchelin.domain.store.controller;

import com.example.fourchelin.common.template.RspTemplate;
import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping()
    public RspTemplate<StorePageResponse> findStores(@RequestParam(required = false) String keyword,
                                                     @RequestParam(required = false) String category,
                                                     @RequestParam(required = false) Integer star,
                                                     @RequestParam(required = false, defaultValue = "1") int page,
                                                     @RequestParam(required = false, defaultValue = "10") int size,
                                                     @RequestParam(required = false, defaultValue = "updateAt") String criteria) {

        StorePageResponse res = storeService.findStores(keyword, category, star, page, size, criteria);

        return new RspTemplate<>(HttpStatus.OK, "조회하신 가게 리스트 입니다.", res);
    }
}
