package com.example.fourchelin.domain.waiting.controller;

import com.example.fourchelin.common.template.RspTemplate;
import com.example.fourchelin.domain.waiting.dto.request.WaitingRequest;
import com.example.fourchelin.domain.waiting.dto.response.WaitingResponse;
import com.example.fourchelin.domain.waiting.service.WaitingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/waiting")
public class WaitingController {
    private final WaitingService waitingService;

    @PostMapping
    public RspTemplate<WaitingResponse> createWaiting(
            @Valid @RequestBody WaitingRequest request) {   // 유저 확인 추가
        WaitingResponse response = waitingService.createWaiting(request, 1L);
        return new RspTemplate<>(HttpStatus.CREATED, "성공적으로 웨이팅 신청되었습니다.", response);
    }
}
