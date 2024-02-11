package com.umc.networkingService.domain.project.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.request.ProjectUpdateRequest;
import com.umc.networkingService.domain.project.dto.response.ProjectDetailResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectIdResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProjectService{
   ProjectIdResponse createProject(Member member, MultipartFile projectImage, ProjectCreateRequest request);
   ProjectIdResponse updateProject(Member member,UUID projectId, MultipartFile projectImage, ProjectUpdateRequest request);
   ProjectIdResponse deleteProject(UUID projectId);
   ProjectIdResponse searchProject(String projectName);
   ProjectDetailResponse detailProject(UUID projectId);
}
