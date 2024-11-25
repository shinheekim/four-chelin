package com.example.fourchelin.domain.waiting.controller;

import com.example.fourchelin.common.security.UserDetailsImpl;
import com.example.fourchelin.common.template.RspTemplate;
import com.example.fourchelin.domain.waiting.dto.request.WaitingRequest;
import com.example.fourchelin.domain.waiting.dto.response.WaitingResponse;
import com.example.fourchelin.domain.waiting.service.WaitingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/waitings")
public class WaitingController {
    private final WaitingService waitingService;

    @PostMapping
    public RspTemplate<WaitingResponse> createWaiting(
            @Valid @RequestBody WaitingRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        WaitingResponse response = waitingService.createWaiting(request, userDetails.getMember());
        return new RspTemplate<>(HttpStatus.CREATED, "성공적으로 웨이팅 신청되었습니다.", response);
    }
}
