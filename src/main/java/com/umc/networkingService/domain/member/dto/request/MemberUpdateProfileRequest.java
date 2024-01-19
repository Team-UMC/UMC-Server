package com.umc.networkingService.domain.member.dto.request;

import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberUpdateProfileRequest {
    private List<String> positions;
    private List<Part> parts;
    private List<Semester> semesters;
}
