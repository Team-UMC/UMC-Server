package com.umc.networkingService.domain.project.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.response.ProjectAllResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectLike;
import com.umc.networkingService.domain.project.dto.response.ProjectLikeResponse;
import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.domain.project.service.ProjectHeartService;
import com.umc.networkingService.domain.project.service.ProjectService;
import com.umc.networkingService.global.common.base.BaseResponse;
import com.umc.networkingService.global.common.enums.Semester;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Project API", description = "Project 관련 API")
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectHeartService projectHeartService;

    @Operation(summary = "프로젝트 조회 API", description = "프로젝트를 특정 조건(기수, 유형)에 따라 조회화는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @Parameters(value = {
            @Parameter(name = "semester", description = "기수 조건이 있을 경우 입력하는 파라미터입니다."),
            @Parameter(name = "type", description = "유형 조건이 있을 경우 입력하는 파라미터입니다."),
            @Parameter(name = "page", description = "page를 입력하는 파라미터입니다.(0부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 조회되는 프로젝트 수입니다,(미입력 시 기본 10개)"),
    })
    @GetMapping
    public BaseResponse<ProjectAllResponse> inquiryProjects(@RequestParam(required = false) Semester semester,
                                                            @RequestParam(required = false) ProjectType type,
                                                            @PageableDefault(sort = "name", direction = Sort.Direction.ASC)
                                                                @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(projectService.inquiryProjects(semester, type, pageable));
    }

    @Operation(summary = "HOT 프로젝트 조회 API", description = "조회수 기준으로 HOT 프로젝트를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @Parameters(value = {
            @Parameter(name = "page", description = "page를 입력하는 파라미터입니다.(0부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 조회되는 프로젝트 수입니다,(미입력 시 기본 10개)"),
    })
    @GetMapping("/hot")
    public BaseResponse<ProjectAllResponse> inquiryHotProjects(@PageableDefault(sort = "hitCount", direction = Sort.Direction.DESC)
                                                               @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(projectService.inquiryHotProjects(pageable));
    }

    @Operation(summary = "프로젝트 검색 API", description = "키워드가 프로젝트명 또는 태그에 포함된 프로젝트 목록을 검색하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @Parameters(value = {
            @Parameter(name = "keyword", description = "최소 한 글자의 키워드를 입력해야합니다."),
            @Parameter(name = "page", description = "page를 입력하는 파라미터입니다.(0부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 조회되는 프로젝트 수입니다,(미입력 시 기본 10개)"),
    })
    @GetMapping("/search")
    public BaseResponse<ProjectAllResponse> searchProject(@RequestParam String keyword,
                                                          @PageableDefault(sort = "name", direction = Sort.Direction.ASC)
                                                          @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(projectService.searchProject(keyword, pageable));
    }

    @Operation(summary = "프로젝트 상세 조회 API", description = "프로젝트를 상세 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROJECT001", description = "존재하지 않는 프로젝트 입니다.")
    })
    @GetMapping("/{projectId}")
    public BaseResponse<ProjectDetailResponse> inquiryProjectDetail(@PathVariable ("projectId") UUID projectId){
        return BaseResponse.onSuccess(projectService.inquiryProjectDetail(projectId));
    }

    @Operation(summary = "프로젝트 좋아요/취소 API", description = "프로젝트에 좋아요를 누르는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROJECT001", description = "존재하지 않는 프로젝트 입니다.")
    })
    @PostMapping("/{projectId}/like")
    public BaseResponse<ProjectLikeResponse> likeProject(
            @CurrentMember Member member,
            @PathVariable ("projectId") UUID projectId
    ){
        return BaseResponse.onSuccess(projectHeartService.likeProject(member, projectId));
    }


}
