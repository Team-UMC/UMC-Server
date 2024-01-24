package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.dto.request.SemesterPartInfo;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.global.common.base.EntityLoader;

import java.util.List;
import java.util.UUID;

public interface SemesterPartService extends EntityLoader<SemesterPart, UUID> {

    void saveSemesterPartInfos(Member member, List<SemesterPartInfo> semesterPartInfos);

    SemesterPart saveEntity(SemesterPart semesterPart);

}
