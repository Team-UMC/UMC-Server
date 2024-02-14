package com.umc.networkingService.domain.message.facade;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.message.dto.request.MessageRequest;
import com.umc.networkingService.domain.message.dto.response.MessageResponse;
import com.umc.networkingService.domain.message.repository.MessageRoomRepository;
import com.umc.networkingService.domain.message.service.MessageRoomService;
import com.umc.networkingService.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageFacade {

    private final MessageService messageService;
    private final MessageRoomService messageRoomService;

    public MessageResponse.MessageId postMessage(Member sender, UUID messageRoomId, MessageRequest.Message message) {
        return messageService.postMessage(sender, messageRoomId, message, messageRoomService);
    }

    public MessageResponse.MessageId patchMessage(Member member, UUID messageId, MessageRequest.Message message) {
        return messageService.patchMessage(member, messageId, message);
    }

    public MessageResponse.MessageId deleteMessage(Member member, UUID messageId) {
        return messageService.deleteMessage(member, messageId);
    }

    public MessageResponse.JoinMessages joinMessages(Member member, UUID messageRoomId, int page) {
        return messageService.joinMessages(member, messageRoomId, page, messageRoomService);
    }

    public MessageResponse.MessageRoomId createMessageRoom(Member member, UUID receiverId, boolean isAnonymous, String content){
        MessageResponse.MessageRoomId resposnse = messageRoomService.createMessageRoom(member, receiverId, isAnonymous, content);
        messageService.postMessage(member, resposnse.getMessageRoomId(), MessageRequest.Message.builder().message(content).build(), messageRoomService);
        return resposnse;
    }

    public MessageResponse.JoinMessageRooms joinMessageRooms(Member member) {
        return messageRoomService.joinMessageRooms(member, messageService);
    }
}
