package com.umc.networkingService.domain.proposal.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.*;
import com.umc.networkingService.domain.proposal.dto.response.ProposalDetailResponse;
import com.umc.networkingService.domain.proposal.dto.response.ProposalIdResponse;
import com.umc.networkingService.domain.proposal.dto.response.ProposalPagingResponse;
import com.umc.networkingService.domain.proposal.service.ProposalService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "건의글 API", description = "건의글 관련 API")
@RequiredArgsConstructor
@RequestMapping("/proposals")
@RestController
public class ProposalController {
    private final ProposalService proposalService;

    @Operation(summary = "건의글 작성 API", description = "건의글을 작성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "IMAGE001", description = "파일 S3 업로드 실패한 경우")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseResponse <ProposalIdResponse> createProposal(@CurrentMember Member member,
                                                     @Valid @RequestPart ProposalCreateRequest request,
                                                     @RequestPart(name = "proposalImage",required = false) List<MultipartFile> proposalImages){
        return BaseResponse.onSuccess(proposalService.createProposal(member,request,proposalImages));
    }

    @Operation(summary = "건의글 수정 API", description = "건의글을 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROPOSAL001", description = "존재하지 않는 건의글 입니다."),
            @ApiResponse(responseCode = "PROPOSAL002", description = "해당 건의글에 대해 수정 권한이 없습니다."),
            @ApiResponse(responseCode = "IMAGE001", description = "파일 S3 업로드 실패한 경우")
    })
    @PostMapping(value = "/{proposalId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseResponse <ProposalIdResponse> updateProposal(@CurrentMember Member member,
                                                     @PathVariable("proposalId") UUID proposalId,
                                                     @Valid @RequestPart ProposalUpdateRequest request,
                                                     @RequestPart(name = "proposalImage",required = false) List<MultipartFile> proposalImages){
        return BaseResponse.onSuccess(proposalService.updateProposal(member, proposalId, request,proposalImages));
    }

    @Operation(summary = "건의글 삭제 API", description = "건의글을 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROPOSAL001", description = "존재하지 않는 건의글 입니다."),
            @ApiResponse(responseCode = "PROPOSAL003", description = "해당 건의글에 대한 삭제 권한이 없습니다.")
    })
    @DeleteMapping("/{proposalId}")
    BaseResponse <ProposalIdResponse> deleteProposal(@CurrentMember Member member,
                                                     @PathVariable("proposalId") UUID proposalId,
                                                     @Valid @RequestPart ProposalDeleteRequest request){
        return BaseResponse.onSuccess(proposalService.deleteProposal(member,proposalId,request));
    }

    @Operation(summary = "건의글 조회 API", description = "건의글을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROPOSAL001", description = "존재하지 않는 건의글 입니다.")
    })
    @GetMapping
    BaseResponse <ProposalPagingResponse> showProposals(@CurrentMember Member member,
                                                        @PageableDefault (sort = "created_at", direction = Sort.Direction.DESC)
                                                        @Parameter(hidden = true) Pageable pageable){
        return BaseResponse.onSuccess(proposalService.showProposals(member, pageable));
    }

    @Operation(summary = "건의글 상세 조회 API", description = "건의글을 상세 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROPOSAL001", description = "존재하지 않는 건의글 입니다.")
    })
    @GetMapping("/{proposalId}")
    BaseResponse <ProposalDetailResponse> detailProposal(@CurrentMember Member member,
                                                         @PathVariable("proposalId") UUID proposalId){
        return BaseResponse.onSuccess(proposalService.showProposalDetail(member, proposalId));
    }
}
