package com.umc.networkingService.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberUpdateProfileRequest {
    private List<String> campusPositions;
    private List<String> centerPositions;
    private List<SemesterPartInfo> semesterParts;
}
