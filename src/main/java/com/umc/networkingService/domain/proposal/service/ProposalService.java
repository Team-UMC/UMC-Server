package com.umc.networkingService.domain.proposal.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.*;
import com.umc.networkingService.domain.proposal.dto.response.ProposalDetailResponse;
import com.umc.networkingService.domain.proposal.dto.response.ProposalIdResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ProposalService {
    ProposalIdResponse createProposal(Member member, ProposalCreateRequest request);
    ProposalIdResponse updateProposal(Member member, UUID proposalId, ProposalUpdateRequest request);
    ProposalIdResponse deleteProposal(Member member, UUID proposalId, ProposalDeleteRequest request);
    ProposalIdResponse searchProposal(ProposalSearchRequest request);

    ProposalDetailResponse detailProposal(UUID proposalId);
}
