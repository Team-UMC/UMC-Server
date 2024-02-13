package com.umc.networkingService.domain.proposal.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.ProposalCreateRequest;
import com.umc.networkingService.domain.proposal.dto.response.ProposalDetailResponse;
import com.umc.networkingService.domain.proposal.dto.response.ProposalPageResponse;
import com.umc.networkingService.domain.proposal.dto.response.ProposalPagingResponse;
import com.umc.networkingService.domain.proposal.entity.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProposalMapper {
    public Proposal createToProposal(Member member, ProposalCreateRequest request){
        return Proposal.builder()
                .writer(member)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    public ProposalDetailResponse toProposalDetailResponse(Proposal proposal, List<String> proposalImages){
        return ProposalDetailResponse.builder()
                .writer(proposal.getWriter().getNickname() + "/" + proposal.getWriter().getName())
                .profileImage(proposal.getWriter().getProfileImage())
                .title(proposal.getTitle())
                .content(proposal.getContent())
                .proposalImages(proposalImages)
                .createdAt(proposal.getCreatedAt())
                .build();
    }

    public ProposalPageResponse toProposalPageResponse(Proposal proposal){
        return ProposalPageResponse.builder()
                .proposalId(proposal.getId())
                .writer(proposal.getWriter().getNickname() + "/" + proposal.getWriter().getName())
                .profileImage(proposal.getWriter().getProfileImage())
                .title(proposal.getTitle())
                .content(proposal.getContent())
                .createdAt(proposal.getCreatedAt())
                .build();
    }

    public ProposalPagingResponse toProposalPagingResponse(Page<Proposal> proposals){
        List<ProposalPageResponse> ProposalResponses = proposals.map(this::toProposalPageResponse)
                .stream().toList();

        return ProposalPagingResponse.builder()
                .proposalPageResponses(ProposalResponses)
                .page(proposals.getNumber())
                .totalPages(proposals.getNumber())
                .isFirst(proposals.isFirst())
                .isLast(proposals.isLast())
                .build();
    }
}
