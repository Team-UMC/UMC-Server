package com.umc.networkingService.config.initial;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.mascot.entity.Mascot;
import com.umc.networkingService.domain.mascot.repository.MascotRepository;
import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.domain.project.entity.ProjectMember;
import com.umc.networkingService.domain.project.repository.ProjectMemberRepository;
import com.umc.networkingService.domain.project.repository.ProjectRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.ProjectErrorCode;
import com.umc.networkingService.global.common.exception.code.UniversityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final MascotRepository mascotRepository;
    private final UniversityRepository universityRepository;
    private final BranchRepository branchRepository;
    private final BranchUniversityRepository branchUniversityRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    private List<MascotInfo> mascots = Arrays.stream(MascotInfo.values()).toList();
    private List<UniversityInfo> universities = Arrays.stream(UniversityInfo.values()).toList();
    private List<BranchInfo> branches = Arrays.stream(BranchInfo.values()).toList();
    private List<ProjectInfo> projects = Arrays.stream(ProjectInfo.values()).toList();
    private List<ProjectMemberInfo> projectMembers = Arrays.stream(ProjectMemberInfo.values()).toList();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 새로운 마스코트 생성
        insertNewMascots();
        // 새로운 대학교 생성
        insertNewUniversities();
        // 새로운 지부 생성
        List<Branch> newBranches = insertNewBranches();

        // 새로운 지부가 있는 경우 대학교 연결(새로운 기수인 경우)
        if (!newBranches.isEmpty()) {
            updateBranchUniversities();
            connectNewBranchesAndUniversities(newBranches);
        }

        // 새로운 프로젝트 생성
        List<Project> newProjects = insertNewProjects();
        if (!newProjects.isEmpty()) insertProjectMembers(newProjects);
    }

    // 새로운 마스코트를 추가하는 함수
    private void insertNewMascots() {
        mascots.stream()
                .filter(mascot -> !mascotRepository.existsByStartLevelAndType(mascot.getStartLevel(), mascot.getMascotType()))
                .map(mascot -> Mascot.builder()
                        .startLevel(mascot.getStartLevel())
                        .endLevel(mascot.getEndLevel())
                        .type(mascot.getMascotType())
                        .dialogues(mascot.getDialogue())
                        .image(mascot.getImageUrl())
                        .build())
                .forEach(mascotRepository::save);
    }

    // 새로운 대학교를 추가하는 함수
    private List<University> insertNewUniversities() {
        return universities.stream()
                .filter(university -> !universityRepository.existsByName(university.getName()))
                .map(university -> University.builder()
                        .name(university.getName())
                        .semesterLogo(university.getSemesterLogo())
                        .universityLogo(university.getUniversityLogo())
                        .mascot(mascotRepository.findByStartLevelAndType(1, university.getMascotType()).get())
                        .build())
                .map(universityRepository::save)
                .toList();
    }

    // 새로운 지부를 추가하는 함수
    private List<Branch> insertNewBranches() {
        return branches.stream()
                .filter(branch -> !branchRepository.existsByName(branch.getName()))
                .map(branch -> Branch.builder()
                        .name(branch.getName())
                        .description(branch.getDescription())
                        .semester(branch.getSemester())
                        .build())
                .map(branchRepository::save)
                .toList();
    }

    // 새로운 지부에 대학교와 연결(새로운 기수인 경우)
    private void connectNewBranchesAndUniversities(List<Branch> newBranches) {
        newBranches.forEach(this::connectBranchAndUniversities);
    }

    private void connectBranchAndUniversities(Branch branch) {
        BranchInfo branchInfo = BranchInfo.getBranchInfo(branch.getName());

        branchInfo.getUniversities().forEach(universityInfo -> {
            University university = findUniversityByName(universityInfo.getName());
            BranchUniversity branchUniversity = buildBranchUniversity(branch, university);
            branchUniversityRepository.save(branchUniversity);
        });
    }

    private BranchUniversity buildBranchUniversity(Branch branch, University university) {
        return BranchUniversity.builder()
                .branch(branch)
                .university(university)
                .isActive(branch.getSemester() == Semester.findActiveSemester())
                .build();
    }

    // 새로운 기수가 생길 경우 이전 기수들의 isActive 정보 수정
    @Transactional
    public void updateBranchUniversities() {
        List<BranchUniversity> branchUniversities = branchUniversityRepository.findAllByIsActive(Boolean.TRUE);
        for (BranchUniversity branchUniversity : branchUniversities) {
            Branch branch = branchUniversity.getBranch();
            if (branch.getSemester() != Semester.findActiveSemester()) {
                branchUniversity.updateIsActive(Boolean.FALSE);
                branchUniversityRepository.save(branchUniversity);
            }
        }
    }

    private University findUniversityByName(String name) {
        return universityRepository.findByName(name)
                .orElseThrow(() -> new RestApiException(UniversityErrorCode.EMPTY_UNIVERSITY));
    }

    // 새로운 프로젝트를 추가하는 함수
    private List<Project> insertNewProjects() {
        return projects.stream()
                .filter(project -> !projectRepository.existsByName(project.getName()))
                .map(project -> Project.builder()
                        .name(project.getName())
                        .logoImage(project.getImage())
                        .description(project.getDescription())
                        .tags(project.getTags())
                        .semester(project.getSemester())
                        .types(project.getProjectTypes())
                        .build())
                .map(projectRepository::save)
                .toList();
    }

    // 프로젝트에 멤버 추가하는 함수
    private void insertProjectMembers(List<Project> newProjects) {
        projectMembers.stream()
                .map(projectMember -> ProjectMember.builder()
                        .project(findProject(newProjects, projectMember.getProjectName()))
                        .part(projectMember.getPart())
                        .name(projectMember.getName())
                        .nickname(projectMember.getNickname())
                        .build())
                .forEach(projectMemberRepository::save);

    }

    private Project findProject(List<Project> newProjects, String projectName) {
        return newProjects.stream()
                .filter(newProject -> newProject.getName().equals(projectName))
                .findFirst()
                .orElseThrow(() -> new RestApiException(ProjectErrorCode.EMPTY_PROJECT));
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
