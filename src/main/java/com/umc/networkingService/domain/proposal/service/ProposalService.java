package com.umc.networkingService.domain.proposal.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.ProposalCreateRequest;
import com.umc.networkingService.domain.proposal.dto.request.ProposalDeleteRequest;
import com.umc.networkingService.domain.proposal.dto.request.ProposalSearchRequest;
import com.umc.networkingService.domain.proposal.dto.request.ProposalUpdateRequest;
import com.umc.networkingService.domain.proposal.dto.response.ProposalIdResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ProposalService {
    ProposalIdResponse createProposal(Member member, ProposalCreateRequest request);
    ProposalIdResponse updateProposal(Member member, UUID proposalId, ProposalUpdateRequest request);
    ProposalIdResponse deleteProposal(Member member, UUID proposalId, ProposalDeleteRequest request);
    ProposalIdResponse searchProposal(ProposalSearchRequest request);
}
