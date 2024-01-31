package com.umc.networkingService.domain.proposal.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.ProposalCreateRequest;
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
}
