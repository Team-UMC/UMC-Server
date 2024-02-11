package com.umc.networkingService.domain.project.controller;

import com.umc.networkingService.domain.project.dto.response.ProjectAllResponse;
import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.domain.project.service.ProjectService;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectIdResponse;
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

    @Operation(summary = "프로젝트 조회 API", description = "프로젝트를 특정 조건(기수, 유형)에 따라 조회화는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @Parameters(value = {
            @Parameter(name = "semester", description = "기수 조건이 있을 경우 입력하는 파라미터입니다."),
            @Parameter(name = "type", description = "유형 조건이 있을 경우 입력하는 파라미터입니다."),
            @Parameter(name = "page", description = "page를 입력하는 파라미터입니다.(0부터 시작)"),
    })
    @GetMapping
    public BaseResponse<ProjectAllResponse> inquiryProjects(@RequestParam(required = false) Semester semester,
                                                            @RequestParam(required = false) ProjectType type,
                                                            @PageableDefault(sort = "name", direction = Sort.Direction.DESC)
                                                                @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(projectService.inquiryProjects(semester, type, pageable));
    }

    @GetMapping
    @Operation(summary = "프로젝트 검색 API", description = "프로젝트를 검색하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PROJECT001", description = "존재하지 않는 프로젝트 입니다.")
    })
    public BaseResponse<ProjectIdResponse> searchProject(@PathVariable("projectName") String projectName){
        return BaseResponse.onSuccess(projectService.searchProject(projectName));
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


}
