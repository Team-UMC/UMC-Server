package com.umc.networkingService.domain.message.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class MessageResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinMessageRooms { //쪽지함 조회
        List<JoinMessageRoom> messageRooms;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinMessageRoom{
        UUID messageRoomId;
        UUID messageRoomUserId;
        String messageRoomUserName;
        String recentMessage;
        String recentMessageTime;
        Boolean isAnonymous;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinMessages { //쪽지 조회
        List<JoinMessage> JoinMessage;
        UUID messageRoomId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinMessage
    {
        UUID messageId;
        String message;
        String messageTime;
        String messageMemberName;
        UUID messageMemberId;
        Boolean isAnonymous; //해당 메시지의 주인이 익명인지
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageRoomId
    {
        UUID messageRoomId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageId
    {
        UUID messageId;
    }
}
