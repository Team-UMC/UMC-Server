package com.umc.networkingService.domain.proposal.dto.response;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProposalPageResponse {
    private UUID proposalId;
    private String profileImage;
    private String writer;
    private String title;
    private String content;
    private int commentCount;
    private LocalDateTime createdAt;
}
