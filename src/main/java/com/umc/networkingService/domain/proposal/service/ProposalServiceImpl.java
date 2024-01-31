package com.umc.networkingService.domain.proposal.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.ProposalCreateRequest;
import com.umc.networkingService.domain.proposal.dto.request.ProposalUpdateRequest;
import com.umc.networkingService.domain.proposal.dto.response.ProposalIdResponse;
import com.umc.networkingService.domain.proposal.entity.Proposal;
import com.umc.networkingService.domain.proposal.mapper.ProposalMapper;
import com.umc.networkingService.domain.proposal.repository.ProposalRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

    private ProposalMapper proposalMapper;
    private ProposalRepository proposalRepository;
    @Override
    public ProposalIdResponse createProposal(Member member, ProposalCreateRequest request) {
        // 전달 받은 정보를 가지고 새 건의글로 생성
        Proposal newProposal = proposalMapper.createToProposal(member,request);

        // 새로 생성한 건의글을 DB에 저장
        // Todo: ProposalImage 처리 로직..
        Proposal savedProposal = proposalRepository.save(newProposal);

        return new ProposalIdResponse(savedProposal.getId());
    }

    @Override
    public ProposalIdResponse updateProposal(Member member, UUID proposalId, ProposalUpdateRequest request){
        // 등록되지 않은 건의글을 수정하려 하는 경우, 예외처리 메세지 반환
        Proposal proposal = proposalRepository.findById(proposalId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_PROPOSAL));

        // 건의글의 작성자가 아닌 멤버가 글을 수정하려는 경우, 예외처리 메세지 반환
        // Todo: 작성자 외에 수정 권한이 있는 멤버 확인하는 로직
        if(!proposal.getWriter().equals(member))
            throw new RestApiException(ErrorCode.NO_AUTHORIZATION_PROPOSAL);

        proposal.update(request);
        return new ProposalIdResponse(proposal.getId());
    }
}
