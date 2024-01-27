package com.umc.networkingService.domain.member.dto;

import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SemesterPartInfo {
    private Part part;
    private Semester semester;
}