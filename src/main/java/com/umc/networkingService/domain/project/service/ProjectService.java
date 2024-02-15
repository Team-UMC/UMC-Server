package com.umc.networkingService.domain.project.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.request.ProjectUpdateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectAllResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectIdResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectLikeResponse;
import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.global.common.base.EntityLoader;
import com.umc.networkingService.global.common.enums.Semester;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProjectService extends EntityLoader<Project, UUID> {
   ProjectIdResponse createProject(Member member, MultipartFile projectImage, ProjectCreateRequest request);
   ProjectIdResponse updateProject(Member member,UUID projectId, ProjectUpdateRequest request);
   ProjectIdResponse deleteProject(UUID projectId);
   ProjectAllResponse inquiryProjects(Member member, Semester semester, ProjectType type, Pageable pageable);
   ProjectAllResponse inquiryHotProjects(Member member, Pageable pageable);
   ProjectAllResponse searchProject(Member member, String keyword, Pageable pageable);
   ProjectDetailResponse inquiryProjectDetail(Member member, UUID projectId);

   int heartCountUp(Project project);
   int heartCountdown(Project project);
}
