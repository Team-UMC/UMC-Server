package com.umc.networkingService.domain.proposal.dto.response;

import com.umc.networkingService.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProposalDetailResponse {
    private Member writer;
    private String title;
    private String content;
}
