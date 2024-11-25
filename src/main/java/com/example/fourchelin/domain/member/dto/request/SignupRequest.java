package com.example.fourchelin.domain.member.dto.request;

import com.example.fourchelin.domain.member.enums.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
        @Pattern(regexp = "\\d{10,11}", message = "전화번호는 10~11자리 숫자여야 합니다.")
        String phone,

        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        @Size(min = 2, max = 20, message = "닉네임은 2~20자여야 합니다.")
        String nickname,

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
        String password,

        @NotNull(message = "역할은 필수 입력 항목입니다.")
        MemberRole role) {


}
