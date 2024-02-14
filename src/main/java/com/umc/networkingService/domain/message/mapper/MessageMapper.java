package com.umc.networkingService.domain.message.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.message.dto.response.MessageResponse;
import com.umc.networkingService.domain.message.entity.Message;
import com.umc.networkingService.domain.message.entity.MessageRoom;
import org.springframework.stereotype.Component;


public class MessageMapper {

    //쪽지방 리스트 조회ㅓ
    public static MessageResponse.JoinMessageRoom toJoinMessageRoom(MessageRoom messageRoom, Message recentMessage, Member messageRoomUser){
        return MessageResponse.JoinMessageRoom.builder()
                .messageRoomId(messageRoom.getId())
                .messageRoomUserId(messageRoomUser.getId())
                .messageRoomUserName(messageRoomUser.getNickname()+messageRoomUser.getName())
                .recentMessage(recentMessage.getContent())
                .recentMessageTime(recentMessage.getCreatedAt().toString())
                .isAnonymous(messageRoom.getIsAnonymous()&&messageRoom.getSender().getId().equals(messageRoomUser.getId()))
                .build();

    }
}
