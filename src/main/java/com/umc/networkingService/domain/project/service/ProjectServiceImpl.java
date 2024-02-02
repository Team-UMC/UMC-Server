package com.umc.networkingService.domain.project.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.project.mapper.ProjectMapper;
import com.umc.networkingService.domain.project.repository.ProjectRepository;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.request.ProjectUpdateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectIdResponse;
import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final S3FileComponent s3FileComponent;
    private final MemberService memberService;

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    @Override
    public ProjectIdResponse createProject(Member member, MultipartFile projectImage, ProjectCreateRequest request) {

        // 프로젝트 이미지 S3 저장
        String imageUrl = s3FileComponent.uploadFile("project", projectImage);

        List<Member> projectMembers = request.getMembers().stream()
                .map(memberService::loadEntity)
                .toList();

        Project newProject = projectMapper.createToProject(imageUrl, projectMembers, request);

        Project savedProject = projectRepository.save(newProject);

        return new ProjectIdResponse(savedProject.getId());
    }

    @Override
    @Transactional
    public ProjectIdResponse updateProject(Member member, UUID projectId, MultipartFile projectImage, ProjectUpdateRequest request){
        // 등록되지 않은 프로젝트를 수정하려고 하는 경우에, 예외처리 메세지 반환
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_PROJECT));

        // Todo: 해당 프로젝트의 수정 권한이 없을 경우, 에러처리 메세지 반환

        // Todo: 해당 프로젝트에서 수정해야 하는 정보를 받아서 업데이트 (일단 이름이랑 슬로건만 받는 상태로 구현)
        project.updateProject(request.getName(),request.getSlogan());

        return new ProjectIdResponse(project.getId());
    }

    @Override
    @Transactional
    public ProjectIdResponse deleteProject(UUID projectId){
        // 등록되지 않은 프로젝트를 삭제하려고 하는 경우, 예외처리 메세지 반환
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_PROJECT));

        // Todo: 해당 프로젝트의 수정 권한이 없을 경우, 에러처리 메세지 반환

        project.delete();

        return new ProjectIdResponse();
    }

    @Override
    @Transactional
    public ProjectIdResponse searchProject(String projectName){
        // 등록되지 않은 프로젝트를 검색하는 경우, 예외처리 메시지 반환
        Project project = projectRepository.findByName(projectName).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_PROJECT));

        // 검색한 프로젝트의 id 반환
        return new ProjectIdResponse(project.getId());
    }

    @Override
    public ProjectDetailResponse detailProject(UUID projectId){
        // 유효하지 않은 프로젝트 id를 받은 경우, 예외처리 메시지 반환
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_PROJECT));

        // 프로젝트 id를 통해 해당 프로젝트의 디테일 데이터 반환
        // Todo: 로고 이미지 반환
        return projectMapper.detailProject(project);
    }
}