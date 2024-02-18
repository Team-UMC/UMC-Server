package com.umc.networkingService.domain.proposal.mapper;

import com.umc.networkingService.domain.proposal.entity.Proposal;
import com.umc.networkingService.domain.proposal.entity.ProposalImage;
import org.springframework.stereotype.Component;

@Component
public class ProposalImageMapper {
    public ProposalImage toProposalImage(Proposal proposal, String url){
        return ProposalImage.builder()
                .proposal(proposal)
                .url(url)
                .build();
    }
}
