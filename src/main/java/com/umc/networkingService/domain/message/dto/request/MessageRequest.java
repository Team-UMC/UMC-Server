package com.umc.networkingService.domain.message.dto.request;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class MessageRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 쪽지 생성, 수정
    public static class Message {
        //todo: 글자수 제한
        String message;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 쪽지방 생성
    public static class StartMessageRoom{
        @NonNull
        UUID messageRoomUserId;
        Boolean isAnonymous; //기본적으로 실명
        @NonNull
        String messageContent;
    }
}
