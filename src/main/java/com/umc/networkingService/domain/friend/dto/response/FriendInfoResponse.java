package com.umc.networkingService.domain.friend.dto.response;

import com.umc.networkingService.domain.member.dto.SemesterPartInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendInfoResponse {
    private UUID memberId;
    private String nickname;
    private String name;
    private String profileImage;
    private String universityName;
    private List<String> campusPositions;
    private List<String> centerPositions;
    private List<SemesterPartInfo> semesterParts;
}
