package com.umc.networkingService.domain.university.dto.request;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public class UniversityRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 대학교 생성, 수정
    public static class universityInfo {
        //todo: 마스코트 처리
        String universityName;
        MultipartFile universityLogo;
        MultipartFile semesterLogo;
        Semester semester;
    }

}
