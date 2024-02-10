package com.umc.networkingService.domain.proposal.service;

import com.umc.networkingService.domain.proposal.entity.Proposal;
import com.umc.networkingService.domain.proposal.entity.ProposalImage;
import com.umc.networkingService.domain.proposal.mapper.ProposalImageMapper;
import com.umc.networkingService.domain.proposal.repository.ProposalImageRepository;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor

public class ProposalImageServiceImpl implements  ProposalImageService{

    private final ProposalImageMapper proposalImageMapper;
    private final ProposalImageRepository proposalImageRepository;
    private final S3FileComponent s3FileComponent;

    @Override
    public void uploadProposalImage(Proposal proposal, List<MultipartFile> proposalImages){
        // 건의글 이미지 업로드
        proposalImages.forEach(proposalImage -> proposalImageRepository.save(proposalImageMapper
                .toProposalImage(proposal, s3FileComponent.uploadFile("Proposal",proposalImage))));
    }

    @Override
    public void updateProposalImage(Proposal proposal, List<MultipartFile> proposalImages){
        List<ProposalImage> Images = findProposalImage(proposal);

        Images.forEach(proposalImage -> {
            s3FileComponent.deleteFile(proposalImage.getUrl());
            proposalImageRepository.deleteById(proposal.getId());
        });

        if(Images != null){
           uploadProposalImage(proposal,proposalImages);
        }
    }

    @Override
    public List<ProposalImage> findProposalImage(Proposal proposal){
        return proposalImageRepository.findAllByProposal(proposal);
    }

    @Override
    public void deleteProposalImage(Proposal proposal){
        List<ProposalImage> proposalImages = findProposalImage(proposal);

        proposalImages.forEach(ProposalImage::delete);
    }

}
