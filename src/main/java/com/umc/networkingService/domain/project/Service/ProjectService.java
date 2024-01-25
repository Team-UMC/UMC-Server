package com.umc.networkingService.domain.project.Service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest;
import com.umc.networkingService.domain.project.dto.request.ProjectCreateRequest.CreateProject;
import com.umc.networkingService.domain.project.dto.response.ProjectCreateResponse;
import com.umc.networkingService.domain.project.dto.response.ProjectCreateResponse.ProjectId;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectService{
   ProjectCreateResponse createProject(Member member, MultipartFile projectImage, ProjectCreateRequest request);
}
