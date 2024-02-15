package com.umc.networkingService.domain.project.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.response.ProjectLikeResponse;
import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.domain.project.entity.ProjectHeart;
import com.umc.networkingService.domain.project.repository.ProjectHeartRepository;
import com.umc.networkingService.domain.project.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectHeartServiceImpl implements ProjectHeartService {

    private final ProjectHeartRepository projectHeartRepository;

    private final ProjectService projectService;

    @Override
    @Transactional
    public ProjectLikeResponse likeProject(Member member, UUID projectId) {
        Project project = projectService.loadEntity(projectId);
        if(projectHeartRepository.existsByMemberIdAndProjectId(member.getId(),projectId)){ //이미 하트가 있는지 확인
            projectHeartRepository.deleteByMemberIdAndProjectId(member.getId(),projectId); //하트 엔티티 삭제
            return ProjectLikeResponse.builder()
                    .likeCount(projectService.heartCountdown(project))
                    .isLike(false).build();

        } else{
            projectHeartRepository.save( //하트 엔티티 생성
                    ProjectHeart.builder()
                                    .member(member)
                                    .project(project)
                            .build()
            );
            return ProjectLikeResponse.builder()
                    .likeCount(projectService.heartCountUp(project))
                    .isLike(true).build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLikeProject(Member member, UUID projectId) {
        return projectHeartRepository.existsByMemberIdAndProjectId(member.getId(),projectId);
    }


}
