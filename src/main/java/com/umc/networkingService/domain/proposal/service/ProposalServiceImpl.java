package com.umc.networkingService.domain.proposal.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.ProposalCreateRequest;
import com.umc.networkingService.domain.proposal.dto.response.ProposalIdResponse;
import com.umc.networkingService.domain.proposal.entity.Proposal;
import com.umc.networkingService.domain.proposal.mapper.ProposalMapper;
import com.umc.networkingService.domain.proposal.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


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
}
