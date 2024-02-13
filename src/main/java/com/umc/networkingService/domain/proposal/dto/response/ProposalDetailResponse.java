package com.umc.networkingService.domain.proposal.dto.response;

import com.umc.networkingService.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProposalDetailResponse {
    private String writer;
    private String profileImage;
    private String title;
    private String content;
    private List<String> proposalImages;
    private LocalDateTime createdAt;
}
