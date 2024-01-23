package com.umc.networkingService.domain.member.dto.response;

import com.umc.networkingService.domain.member.dto.SemesterPartInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class MemberSearchInfoResponse {
    private UUID memberId;
    private String universityName;
    private List<String> campusPositions;
    private List<String> centerPositions;
    private List<SemesterPartInfo> semesterParts;
}
