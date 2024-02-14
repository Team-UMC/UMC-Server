package com.umc.networkingService.domain.message.service;


import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.message.dto.request.MessageRequest;
import com.umc.networkingService.domain.message.dto.response.MessageResponse;
import com.umc.networkingService.domain.message.entity.Message;
import com.umc.networkingService.domain.message.entity.MessageRoom;
import com.umc.networkingService.domain.message.repository.MessageRepository;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.GlobalErrorCode;
import com.umc.networkingService.global.common.exception.code.MessageErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;


    //쪽지 보내기
    @Override
    public MessageResponse.MessageId postMessage(Member sender, UUID messageRoomId, MessageRequest.Message message, MessageRoomService messageRoomService){
        if(message.getMessage().isEmpty())
            throw new RestApiException(MessageErrorCode.EMPTY_MESSAGE);

        return MessageResponse.MessageId.builder()
                .messageId(
                        messageRepository.save(
                                Message.builder()
                                        .messageRoom(messageRoomService.findByMessageRoomId(messageRoomId))
                                        .content(message.getMessage())
                                        .isSender(sender.getId().equals(messageRoomService.findByMessageRoomId(messageRoomId).getSender().getId()))
                                        .build()).getId()
                ).build();
    }

    //쪽지 수정
    @Override
    public MessageResponse.MessageId patchMessage(Member member, UUID messageId, MessageRequest.Message message){
        messageRepository.findById(messageId)
                .orElseThrow(() -> new RestApiException(MessageErrorCode.NOT_FOUND_MESSAGE))
                .updateContent(message.getMessage());
        return MessageResponse.MessageId.builder().messageId(messageId).build();
    }

    //쪽지 삭제
    @Override
    public MessageResponse.MessageId deleteMessage(Member member, UUID messageId){
        messageRepository.findById(messageId)
                .orElseThrow(() -> new RestApiException(MessageErrorCode.NOT_FOUND_MESSAGE))
                .delete();
        return MessageResponse.MessageId.builder().messageId(messageId).build();
    }

    //쪽지방의 메시지 리스트 조회
    @Override
    public MessageResponse.JoinMessages joinMessages(Member member ,UUID messageRoomId, int page, MessageRoomService messageRoomService){

        if(page < 0)
            throw new RestApiException(GlobalErrorCode._INTERNAL_PAGE_ERROR);

        MessageRoom messageRoom = messageRoomService.findByMessageRoomId(messageRoomId);

        Page<Message> messages = messageRepository.findAllByMessageRoomIdOrderByCreatedAtDesc(
                messageRoomId, PageRequest.of(page, 20)); //20개씩 페이징

        return MessageResponse.JoinMessages.builder()
                .messageRoomId(messageRoomId)
                .JoinMessage(
                        messages.getContent().stream()
                                .map(message -> MessageResponse.JoinMessage.builder()
                                        .messageId(message.getId())
                                        .message(message.getContent())
                                        .messageTime(message.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                                        .messageMemberName( //해당 메시지를 보낸 사람 이름
                                                Boolean.TRUE.equals(message.getIsSender())
                                                        ? messageRoom.getSender().getNickname()+"/"+messageRoom.getSender().getName()
                                                        : messageRoom.getReceiver().getNickname()+"/"+messageRoom.getReceiver().getName()
                                        )
                                        .messageMemberId( //해당 메시지를 보낸 사람 아이디
                                                Boolean.TRUE.equals(message.getIsSender())
                                                        ? messageRoom.getSender().getId()
                                                        : messageRoom.getReceiver().getId()
                                        )            //해당 메시지를 보낸 사람이 익명인지 여부
                                        .isAnonymous(message.getIsSender()&&messageRoom.getIsAnonymous())
                                        .build()
                                ).toList()
                )
                .build();

    }


    //가장 최신의 메시지 찾기
    @Override
    public Message findRecentMessageByMessageRoom(MessageRoom messageRoom){
        return messageRepository.findTopByMessageRoomOrderByCreatedAtDesc(messageRoom);
    }
}
