package com.umc.networkingService.domain.proposal.repository;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProposalRepository extends JpaRepository<Proposal, UUID> {
    Optional <Proposal> findById(Member member);
    Optional <Proposal> findBytile(String title);
}
