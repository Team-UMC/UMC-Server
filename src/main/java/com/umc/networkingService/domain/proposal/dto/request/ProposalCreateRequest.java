package com.umc.networkingService.domain.proposal.dto.request;

import com.umc.networkingService.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProposalCreateRequest {
    private Member writer;
    private String title;
    private String content;
}