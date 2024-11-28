package com.example.fourchelin.domain.store.controller;

import com.example.fourchelin.common.template.RspTemplate;
import com.example.fourchelin.domain.store.dto.response.StorePageResponse;
import com.example.fourchelin.domain.store.enums.StoreStatus;
import com.example.fourchelin.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/v1/stores")
    public RspTemplate<StorePageResponse> searchStore(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer star,
            @RequestParam(required = false) StoreStatus status) {

        if (star == null && status == null){
            throw new IllegalArgumentException("star 또는 status 중 하나라도 넣어주세요.");
        }

        // 필터링된 업체 리스트 반환
        StorePageResponse res = storeService.searchStore(page, size, star, status);
        return new RspTemplate<>(HttpStatus.OK, res);
    }

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

    //csv를 database에 입력
    @PostMapping("/collection")
    public RspTemplate<Void> uploadCsvToDatabase(@RequestParam String filePath) {
        try {
            storeService.insertDataFromCsv(filePath);
            return new RspTemplate<>(HttpStatus.OK, "csv 파일 데이터가 성공적으로 데이터베이스에 입력되었습니다");
        }catch (Exception e){
            return new RspTemplate<>(HttpStatus.INTERNAL_SERVER_ERROR,"csv 파일 입력 중 오류 발생:"+e.getMessage());
        }
    }
}
