package com.umc.networkingService.domain.message.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.message.dto.request.MessageRequest;
import com.umc.networkingService.domain.message.dto.response.MessageResponse;
import com.umc.networkingService.domain.message.entity.Message;
import com.umc.networkingService.domain.message.entity.MessageRoom;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface MessageService {
    @Transactional
    MessageResponse.MessageId postMessage(Member sender, UUID messageRoomId, MessageRequest.Message message, MessageRoomService messageRoomService);

    //쪽지 수정
    @Transactional
    MessageResponse.MessageId patchMessage(Member member, UUID messageId, MessageRequest.Message message);

    //쪽지 삭제
    @Transactional
    MessageResponse.MessageId deleteMessage(Member member, UUID messageId);

    //쪽지방의 메시지 리스트 조회
    @Transactional(readOnly = true)
    MessageResponse.JoinMessages joinMessages(Member member ,UUID messageRoomId, int page, MessageRoomService messageRoomService);


    //가장 최신의 메시지 찾기
    @Transactional(readOnly = true)
    Message findRecentMessageByMessageRoom(MessageRoom messageRoom);
}
