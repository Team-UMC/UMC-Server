package com.umc.networkingService.domain.proposal.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.ProposalCreateRequest;
import com.umc.networkingService.domain.proposal.dto.response.ProposalDetailResponse;
import com.umc.networkingService.domain.proposal.entity.Proposal;
import org.springframework.stereotype.Component;

@Component
public class ProposalMapper {
    public Proposal createToProposal(Member member, ProposalCreateRequest request){
        return Proposal.builder()
                .writer(member)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    public ProposalDetailResponse detailProposal(Proposal proposal){
        return ProposalDetailResponse.builder()
                .writer(proposal.getWriter())
                .title(proposal.getTitle())
                .content(proposal.getContent())
                .build();
    }
}
