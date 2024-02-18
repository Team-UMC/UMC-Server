package com.umc.networkingService.domain.proposal.repository;

import com.umc.networkingService.domain.proposal.entity.Proposal;
import com.umc.networkingService.domain.proposal.entity.ProposalImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProposalImageRepository extends JpaRepository<ProposalImage, UUID> {
    List<ProposalImage> findAllByProposal(Proposal proposal);
}
