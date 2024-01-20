package com.umc.networkingService.domain.member.dto.response;

import com.umc.networkingService.domain.member.entity.MemberRelation;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberInquiryProfileResponse {
    private String profileImage;
    private String universityName;
    private String name;
    private String nickname;
    private List<Part> parts;
    private List<Semester> semesters;
    private String statusMessage;
    private MemberRelation owner;
}
