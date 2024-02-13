package com.umc.networkingService.domain.message.dto.request;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public class MessageRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 쪽지 생성, 수정
    public static class Message {
        //todo: 글자수 제한
        String message;
    }
}
