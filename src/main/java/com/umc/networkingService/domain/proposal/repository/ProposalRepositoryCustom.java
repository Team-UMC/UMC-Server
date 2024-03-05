package com.umc.networkingService.domain.proposal.repository;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.entity.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProposalRepositoryCustom {
    Page<Proposal> findAllProposals(Member member, Pageable pageable);
}
