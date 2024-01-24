package com.umc.networkingService.domain.member.dto.response;

import com.umc.networkingService.domain.member.dto.request.SemesterPartInfo;
import com.umc.networkingService.domain.member.entity.MemberRelation;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class MemberInquiryProfileResponse {
    private UUID memberId;
    private String profileImage;
    private String universityName;
    private String name;
    private String nickname;
    private List<SemesterPartInfo> semesterParts;
    private String statusMessage;
    private MemberRelation owner;
}
