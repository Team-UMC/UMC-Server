package com.umc.networkingService.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignUpRequest {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickname;
    @Size(min = 1, message = "최소 하나의 기수별 파트가 필요합니다.")
    private List<SemesterPartInfo> semesterParts;
    private String universityName;
    private List<String> campusPositions;
    private List<String> centerPositions;
}
