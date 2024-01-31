package com.umc.networkingService.domain.proposal.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.ProposalCreateRequest;
import com.umc.networkingService.domain.proposal.dto.response.ProposalIdResponse;
import com.umc.networkingService.domain.proposal.service.ProposalService;
import com.umc.networkingService.global.common.base.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

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
}
