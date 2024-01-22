package com.umc.networkingService.domain.member.dto.request;

import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.validation.constraints.NotBlank;
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
    private List<SemesterPartInfo> semesterParts;
    private String universityName;
    private List<String> campusPositions;
    private List<String> centerPositions;
}
