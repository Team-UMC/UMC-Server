package com.umc.networkingService.domain.member.dto.request;

import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberSignUpRequest {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickname;
    @Size(min = 1, message = "최소 1개의 파트를 선택해야 합니다.")
    private List<Part> parts;
    @Size(min = 1, message = "최소 1개의 기수를 선택해야 합니다.")
    private List<Semester> semesters;
    private String universityName;
    private List<String> campusPositions;
    private List<String> centerPositions;
}
