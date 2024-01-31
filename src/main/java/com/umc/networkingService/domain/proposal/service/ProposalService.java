package com.umc.networkingService.domain.proposal.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.ProposalCreateRequest;
import com.umc.networkingService.domain.proposal.dto.response.ProposalIdResponse;

public interface ProposalService {
    ProposalIdResponse createProposal(Member member, ProposalCreateRequest request);
}
