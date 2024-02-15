package com.umc.networkingService.domain.project.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.project.dto.response.ProjectLikeResponse;

import java.util.UUID;

public interface ProjectHeartService {
    ProjectLikeResponse likeProject(Member member, UUID projectId);
}
