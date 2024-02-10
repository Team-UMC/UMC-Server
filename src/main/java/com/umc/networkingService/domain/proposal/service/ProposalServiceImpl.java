package com.umc.networkingService.domain.proposal.service;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.*;
import com.umc.networkingService.domain.proposal.dto.response.ProposalDetailResponse;
import com.umc.networkingService.domain.proposal.dto.response.ProposalIdResponse;
import com.umc.networkingService.domain.proposal.dto.response.ProposalPagingResponse;
import com.umc.networkingService.domain.proposal.entity.Proposal;
import com.umc.networkingService.domain.proposal.mapper.ProposalMapper;
import com.umc.networkingService.domain.proposal.repository.ProposalRepository;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

    private final ProposalMapper proposalMapper;
    private final ProposalRepository proposalRepository;
    private final ProposalImageService proposalImageService;
    @Override
    public ProposalIdResponse createProposal(Member member, ProposalCreateRequest request, List<MultipartFile> proposalImage) {
        // 전달 받은 정보를 가지고 새 건의글로 생성
        Proposal newProposal = proposalMapper.createToProposal(member,request);

        // 새로 생성한 건의글을 DB에 저장
        Proposal savedProposal = proposalRepository.save(newProposal);

        // 건의글 이미지 처리
        if(proposalImage != null)
            proposalImageService.uploadProposalImage(savedProposal, proposalImage);

        return new ProposalIdResponse(savedProposal.getId());
    }

    @Override
    public ProposalIdResponse updateProposal(Member member, UUID proposalId, ProposalUpdateRequest request, List<MultipartFile> proposalImages){
        // 등록되지 않은 건의글을 수정하려 하는 경우, 예외처리 메세지 반환
        Proposal proposal = proposalRepository.findById(proposalId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_PROPOSAL));

        // 건의글의 작성자가 아닌 멤버가 글을 수정하려는 경우, 예외처리 메세지 반환
        if(!proposal.getWriter().equals(member))
            throw new RestApiException(ErrorCode.NO_UPDATE_AUTHORIZATION_PROPOSAL);

        proposal.update(request);

        proposalImageService.updateProposalImage(proposal,proposalImages);

        return new ProposalIdResponse(proposal.getId());
    }

    @Override
    public ProposalIdResponse deleteProposal(Member member, UUID proposalId, ProposalDeleteRequest request){
        // 등록되지 않은 건의글을 삭제하려 하는 경우, 예외처리 메세지 반환
        Proposal proposal = proposalRepository.findById(proposalId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_PROPOSAL));

        // 건의글의 작성자가 아닌 멤버가 글을 수정하려는 경우, 예외처리 메세지 반환
        if(!proposal.getWriter().getId().equals(member.getId())) {
            // 운영진 권한이 있는 경우에는 삭제 가능
            if(member.getRole().getPriority() == Role.MEMBER.getPriority())
                throw new RestApiException(ErrorCode.NO_DELETE_AUTHORIZATION_PROPOSAL);
        }
        proposalImageService.deleteProposalImage(proposal);
        proposal.delete();
        return new ProposalIdResponse(proposal.getId());
    }

    @Override
    public ProposalPagingResponse showProposals(Member member, Pageable pageable){
        return proposalMapper.toProposalPagingResponse(proposalRepository.findAllProposals(member,pageable));
    }

    @Override
    public ProposalDetailResponse detailProposal(UUID proposalId){
        // 유효하지 않은 건의글인 경우, 예외처리 메세지 반환
        Proposal proposal = proposalRepository.findById(proposalId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_PROPOSAL));

        // 건의글의 세부 정보 조회
        // Todo: proposalImage 처리..
        return proposalMapper.detailProposal(proposal);
    }
}
