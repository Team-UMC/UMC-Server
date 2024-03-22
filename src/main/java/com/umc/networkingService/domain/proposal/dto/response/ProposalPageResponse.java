package com.umc.networkingService.domain.proposal.dto.response;

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
    private LocalDateTime createdAt;
}
