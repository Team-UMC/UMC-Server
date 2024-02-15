package com.umc.networkingService.domain.project.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.request.ProjectUpdateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectAllResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectIdResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectLikeResponse;
import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.domain.project.entity.ProjectMember;
import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.domain.project.mapper.ProjectMapper;
import com.umc.networkingService.domain.project.repository.ProjectMemberRepository;
import com.umc.networkingService.domain.project.repository.ProjectRepository;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final S3FileComponent s3FileComponent;

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    private final ProjectHeartService projectHeartService;

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
    public ProjectIdResponse updateProject(Member member, UUID projectId, ProjectUpdateRequest request){

        Project project = loadEntity(projectId);

        // Todo: 해당 프로젝트에서 수정해야 하는 정보를 받아서 업데이트 (일단 이름, 설명, 태그만 받는 상태로 구현)
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
    public ProjectAllResponse inquiryProjects(Member member, Semester semester, ProjectType type, Pageable pageable) {
        Page<Project> projects;

        // 기수 조건과 타입 조건의 유무에 따라서 조회
        if (semester == null) {
            projects = Optional.ofNullable(type)
                    .map(t -> projectRepository.findAllByTypesContains(t, pageable))
                    .orElseGet(() -> projectRepository.findAll(pageable));
        } else {
            projects = Optional.ofNullable(type)
                    .map(t -> projectRepository.findAllBySemesterAndTypesContains(semester, t, pageable))
                    .orElseGet(() -> projectRepository.findAllBySemester(semester, pageable));
        }

        return new ProjectAllResponse(
                projects.stream().map(
                        project -> projectMapper.toProjectInfo(project, projectHeartService.isLikeProject(member, project.getId()))
                        ).toList(),
                projects.hasNext()
        );
    }

    @Override
    public ProjectAllResponse inquiryHotProjects(Member member, Pageable pageable) {
        // 조회수 1점, 하트수 3점으로 점수를 계산해 내림차순 정렬
        List<Project> projects = projectRepository.findAll();
        List<Project> hotProjects = projects.stream()
                .sorted(Comparator.comparingLong(this::calculateScore).reversed()) //점수 내림차순 정렬
                .skip(pageable.getOffset()) // page * size 만큼 skip
                .limit(pageable.getPageSize()) // size 만큼 limit
                .toList();


        return ProjectAllResponse.builder()
                .projects(hotProjects.stream().map(
                        project -> projectMapper.toProjectInfo(project, projectHeartService.isLikeProject(member, project.getId()))
                ).toList())
                .build();
    }

    private Long calculateScore(Project project) { // 조회수 1점, 하트수 3점으로 점수를 계산
        return project.getHitCount() + (project.getHeartCount() * 3L);
    }

    @Override
    public ProjectAllResponse searchProject(Member member ,String keyword, Pageable pageable){
        Page<Project> projects = projectRepository.findByNameContainsOrTagContains(keyword, pageable);

        return new ProjectAllResponse(
                projects.stream().map(
                        project -> projectMapper.toProjectInfo(project, projectHeartService.isLikeProject(member, project.getId()))
                ).toList(),
                projects.hasNext()
        );
    }

    @Override
    @Transactional
    public ProjectDetailResponse inquiryProjectDetail(Member member , UUID projectId){
        Project project = loadEntity(projectId);
        // 조회수 증가
        project.addHitCount();

        List<ProjectMember> projectMembers = projectMemberRepository.findAllByProject(project);

        return projectMapper.toProjectDetailResponse(project, projectMembers, projectHeartService.isLikeProject(member, projectId));
    }

    @Override
    public Project loadEntity(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ProjectErrorCode.EMPTY_PROJECT));
    }

    @Override
    @Transactional
    public int heartCountUp(Project project){
        project.addHeartCount();
        return project.getHeartCount();
    }

    @Override
    @Transactional
    public int heartCountdown(Project project){
        project.subtractHeartCount();
        return project.getHeartCount();
    }
}
