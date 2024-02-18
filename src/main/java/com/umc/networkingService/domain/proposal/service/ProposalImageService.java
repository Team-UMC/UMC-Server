package com.umc.networkingService.domain.proposal.service;

import com.umc.networkingService.domain.proposal.entity.Proposal;
import com.umc.networkingService.domain.proposal.entity.ProposalImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProposalImageService {
    void uploadProposalImage(Proposal proposal, List<MultipartFile> proposalImages);
    void updateProposalImage(Proposal proposal, List<MultipartFile> proposalImage);
    List<ProposalImage> findProposalImage(Proposal proposal);
    void deleteProposalImage(Proposal proposal);
}
