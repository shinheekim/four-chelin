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

import java.io.File;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
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


    //csv를 database에 입력
    @PostMapping("/collection")
    public RspTemplate<Void> uploadCsvToDatabase(@RequestParam("file") MultipartFile file) {
        try {
            // 업로드된 파일을 임시 경로에 저장
            File tempFile = File.createTempFile("uploaded-", ".csv");
            file.transferTo(tempFile);

            // 서비스 호출
            storeService.insertDataFromCsv(tempFile.getAbsolutePath());
            return new RspTemplate<>(HttpStatus.OK, "CSV 파일 데이터가 성공적으로 데이터베이스에 입력되었습니다.");
        } catch (Exception e) {
            return new RspTemplate<>(HttpStatus.INTERNAL_SERVER_ERROR, "CSV 파일 처리 중 오류 발생: " + e.getMessage());
        }

    }
}
