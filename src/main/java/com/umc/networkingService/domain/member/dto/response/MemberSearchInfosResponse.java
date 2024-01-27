package com.umc.networkingService.domain.member.dto.response;

import com.umc.networkingService.domain.member.dto.SemesterPartInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MemberSearchInfosResponse {
    private List<MemberInfo> members;

    @Getter
    @Builder
    public static class MemberInfo {
        private UUID memberId;
        private String universityName;
        private List<String> campusPositions;
        private List<String> centerPositions;
        private List<SemesterPartInfo> semesterParts;
    }
}
