package com.umc.networkingService.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateMyProfileRequest {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickname;
    private String statusMessage;
}
