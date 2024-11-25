package com.example.fourchelin.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
        @Pattern(regexp = "\\d{10,11}", message = "전화번호는 10~11자리 숫자여야 합니다.")
        String phone,

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
        String password) {
}
