package com.umc.networkingService.domain.message.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.message.dto.response.MessageResponse;
import com.umc.networkingService.domain.message.entity.Message;
import com.umc.networkingService.domain.message.entity.MessageRoom;
import com.umc.networkingService.domain.message.mapper.MessageMapper;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.MessageErrorCode;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public interface MessageRoomService {
    //채팅방 생성
    @Transactional
    MessageResponse.MessageRoomId createMessageRoom(Member member, UUID receiverId, boolean isAnonymous, String content);

    @Transactional(readOnly = true)
    MessageResponse.JoinMessageRooms joinMessageRooms(Member member, MessageService messageService);

    //아이디로 채팅방 찾기
    @Transactional(readOnly = true)
    MessageRoom findByMessageRoomId(UUID messageRoomId);
}
