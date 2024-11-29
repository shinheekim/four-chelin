package com.example.fourchelin.domain.waiting.controller;

import com.example.fourchelin.common.security.UserDetailsImpl;
import com.example.fourchelin.common.template.RspTemplate;
import com.example.fourchelin.domain.waiting.dto.response.WaitingInfoResponse;
import com.example.fourchelin.domain.waiting.dto.request.WaitingRequest;
import com.example.fourchelin.domain.waiting.dto.response.WaitingResponse;
import com.example.fourchelin.domain.waiting.service.WaitingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/waitings")
public class WaitingController {
    private final WaitingService waitingService;

    @PostMapping
    public RspTemplate<Long> createWaiting(
            @Valid @RequestBody WaitingRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long response = waitingService.create(request, userDetails.getMember());
        return new RspTemplate<>(HttpStatus.CREATED, "성공적으로 웨이팅 신청되었습니다.", response);
    }

/*    @PostMapping("/lock")
    public RspTemplate<Long> createLockWaiting(
            @Valid @RequestBody WaitingRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long response = waitingService.createWithRedisLock(request, userDetails.getMember());
        return new RspTemplate<>(HttpStatus.CREATED, "성공적으로 웨이팅 신청되었습니다.", response);
    }*/

    @GetMapping
    public RspTemplate<List<WaitingInfoResponse>> retrieveWaitingInfoList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam String visit){
        List<WaitingInfoResponse> responses = waitingService.retrieveAllByStatus(userDetails.getMember(), visit);
        return new RspTemplate<>(HttpStatus.OK, "성공적으로 웨이팅 정보를 조회하였습니다.", responses);
    }

    @DeleteMapping("/{waitingId}")
    public RspTemplate<String> cancelWaiting(
            @PathVariable Long waitingId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        waitingService.delete(waitingId, userDetails.getMember());
        return new RspTemplate<>(HttpStatus.NO_CONTENT, "성공적으로 웨이팅을 취소하였습니다.");
    }
}
