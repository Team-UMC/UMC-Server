package com.umc.networkingService.domain.proposal.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.ProposalCreateRequest;
import com.umc.networkingService.domain.proposal.dto.request.ProposalUpdateRequest;
import com.umc.networkingService.domain.proposal.dto.response.ProposalIdResponse;
import com.umc.networkingService.domain.proposal.service.ProposalService;
import com.umc.networkingService.global.common.base.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/proposal")
@RestController
public class ProposalController {
    private final ProposalService proposalService;

    @PostMapping
    BaseResponse <ProposalIdResponse> createProposal(@CurrentMember Member member,
                                                     @Valid @RequestPart ProposalCreateRequest request){
        return BaseResponse.onSuccess(proposalService.createProposal(member,request));
    }

    @PostMapping
    BaseResponse <ProposalIdResponse> updateProposal(@CurrentMember Member member,
                                                     @PathVariable("proposalId") UUID proposalId,
                                                     @Valid @RequestPart ProposalUpdateRequest request){
        return BaseResponse.onSuccess(proposalService.updateProposal(member, proposalId, request));
    }
}
