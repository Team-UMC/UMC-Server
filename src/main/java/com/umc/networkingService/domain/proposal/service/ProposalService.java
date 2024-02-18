package com.umc.networkingService.domain.proposal.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.*;
import com.umc.networkingService.domain.proposal.dto.response.ProposalDetailResponse;
import com.umc.networkingService.domain.proposal.dto.response.ProposalIdResponse;
import com.umc.networkingService.domain.proposal.dto.response.ProposalPageResponse;
import com.umc.networkingService.domain.proposal.dto.response.ProposalPagingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProposalService {
    ProposalIdResponse createProposal(Member member, ProposalCreateRequest request, List<MultipartFile> proposalImage);
    ProposalIdResponse updateProposal(Member member, UUID proposalId, ProposalUpdateRequest request, List<MultipartFile> proposalImages);
    ProposalIdResponse deleteProposal(Member member, UUID proposalId, ProposalDeleteRequest request);
    ProposalPagingResponse showProposals(Member member, Pageable pageable);

    ProposalDetailResponse showProposalDetail(Member member, UUID proposalId);
}
