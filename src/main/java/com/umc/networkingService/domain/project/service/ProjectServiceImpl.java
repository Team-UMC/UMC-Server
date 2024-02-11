package com.umc.networkingService.domain.project.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.response.ProjectAllResponse;
import com.umc.networkingService.domain.project.entity.ProjectMember;
import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.domain.project.mapper.ProjectMapper;
import com.umc.networkingService.domain.project.repository.ProjectMemberRepository;
import com.umc.networkingService.domain.project.repository.ProjectRepository;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.request.ProjectUpdateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectIdResponse;
import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.ProjectErrorCode;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final S3FileComponent s3FileComponent;

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public ProjectIdResponse createProject(Member member, MultipartFile projectImage, ProjectCreateRequest request) {

        // 프로젝트 이미지 S3 저장
        if (projectImage == null)
            throw new RestApiException(ProjectErrorCode.EMPTY_PROJECT_IMAGE);
        String imageUrl = s3FileComponent.uploadFile("project", projectImage);

        // 새로운 프로젝트 저장
        Project newProject = projectRepository.save(
                projectMapper.toProject(imageUrl, request)
        );

        // 프로젝트 멤버 저장
        List<ProjectCreateRequest.ProjectMemberInfo> projectMembers = request.getProjectMembers();

        projectMembers.stream()
                .map(projectMember -> projectMapper.toProjectMember(projectMember, newProject))
                .forEach(projectMemberRepository::save);

        return new ProjectIdResponse(newProject.getId());
    }

    @Override
    @Transactional
    public ProjectIdResponse updateProject(Member member, UUID projectId, MultipartFile projectImage, ProjectUpdateRequest request){

        Project project = loadEntity(projectId);

        // Todo: 해당 프로젝트에서 수정해야 하는 정보를 받아서 업데이트 (일단 이름, 슬로건, 설명, 태그만 받는 상태로 구현)
        project.updateProject(request);

        return new ProjectIdResponse(project.getId());
    }

    @Override
    @Transactional
    public ProjectIdResponse deleteProject(UUID projectId){

        Project project = loadEntity(projectId);

        // 프로젝트 이미지 삭제
        s3FileComponent.deleteFile(project.getLogoImage());
        project.deleteProjectImage();

        // 프로젝트 soft delete
        project.delete();

        return new ProjectIdResponse(project.getId());
    }

    @Override
    public ProjectAllResponse inquiryProjects(Semester semester, ProjectType type, Pageable pageable) {
        Page<Project> projects;

        // 기수 조건과 타입 조건의 유무에 따라서 조회
        if (semester == null) {
            projects = Optional.ofNullable(type)
                    .map(t -> projectRepository.findAllByProjectTypeContains(t, pageable))
                    .orElseGet(() -> projectRepository.findAll(pageable));
        } else {
            projects = Optional.ofNullable(type)
                    .map(t -> projectRepository.findAllBySemesterAndProjectTypeContains(semester, t, pageable))
                    .orElseGet(() -> projectRepository.findAllBySemester(semester, pageable));
        }

        return new ProjectAllResponse(
                projects.stream().map(projectMapper::toProjectInfo).toList(),
                projects.hasNext()
        );
    }

    @Override
    public ProjectAllResponse inquiryHotProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);

        return new ProjectAllResponse(
                projects.stream().map(projectMapper::toProjectInfo).toList(),
                projects.hasNext()
        );
    }

    @Override
    public ProjectAllResponse searchProject(String keyword, Pageable pageable){
        Page<Project> projects = projectRepository.findByNameContainsOrTagContains(keyword, pageable);

        return new ProjectAllResponse(
                projects.stream().map(projectMapper::toProjectInfo).toList(),
                projects.hasNext()
        );
    }

    @Override
    @Transactional
    public ProjectDetailResponse inquiryProjectDetail(UUID projectId){
        Project project = loadEntity(projectId);
        // 조회수 증가
        project.addHitCount();

        List<ProjectMember> projectMembers = projectMemberRepository.findAllByProject(project);

        return projectMapper.toProjectDetailResponse(project, projectMembers);
    }

    @Override
    public Project loadEntity(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ProjectErrorCode.EMPTY_PROJECT));
    }
}
