package com.umc.networkingService.domain.message.service;


import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.message.dto.response.MessageResponse;
import com.umc.networkingService.domain.message.entity.Message;
import com.umc.networkingService.domain.message.entity.MessageRoom;
import com.umc.networkingService.domain.message.mapper.MessageMapper;
import com.umc.networkingService.domain.message.repository.MessageRoomRepository;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.MessageErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageRoomServiceImpl implements MessageRoomService{

    private final MessageRoomRepository messageRoomRepository;

    private final MemberService memberService;

    //채팅방 생성
    @Override
    public MessageResponse.MessageRoomId createMessageRoom(Member member, UUID receiverId, boolean isAnonymous, String content){
        Member receiver = memberService.findByMemberId(receiverId);

        //메시지룸 이미 있는지 확인
        if(messageRoomRepository.existsByReceiverAndSenderAndIsAnonymous(receiver, member, isAnonymous)){
            throw new RestApiException(MessageErrorCode.ALREADY_EXIST_MESSAGE_ROOM);
        }

        //메시지룸 생성은 메시지를 보내야지 생성
        if(content.isEmpty()){
            throw new RestApiException(MessageErrorCode.EMPTY_MESSAGE);
        }

        return MessageResponse.MessageRoomId.builder()
                .messageRoomId(messageRoomRepository.save(
                        MessageRoom.builder()
                                .sender(member)
                                .receiver(receiver)
                                .isAnonymous(isAnonymous) //true면 sender가 익명
                                .build()
                ).getId())
                .build();
    }

    @Override
    public MessageResponse.JoinMessageRooms joinMessageRooms(Member member, MessageService messageService){ //메시지룸 리스트 조회
        List<MessageRoom> messageRooms = messageRoomRepository.findAllByReceiverOrSender(member, member);

        return MessageResponse.JoinMessageRooms.builder() //메시지룸 리스트
                .messageRooms(
                        messageRooms.stream()
                                .map(messageRoom -> {
                                    //유저가 receiver인데 isAnonymous가 true면 JoinMessageRoom의 isAnonymous는 true

                                    Member messageRoomUser = (messageRoom.getSender().getId().equals(member.getId())) //request를 보낸 유저가 sender면 receiver, receiver면 sender 반환
                                            ? messageRoom.getReceiver()
                                            : messageRoom.getSender();

                                    Message recentMessage = messageService.findRecentMessageByMessageRoom(messageRoom); //가장 최신의 메시지 찾기

                                    return MessageMapper.toJoinMessageRoom(messageRoom, recentMessage, messageRoomUser); //메시지룸 리스트 조회

                                })
                                .sorted(Comparator.comparing(MessageResponse.JoinMessageRoom::getRecentMessageTime).reversed()) //최신순으로 정렬
                                .toList()
                )
                .build();

        /*
            1. user가 sender인 messageRoom or user가 reciver인 messageRoom 리스트 찾기
            3. 각 메시지 룸 아이디로 최근 메시지 하나 씩 찾음 (messageService로)
            4. 최신 순으로 정렬
            5. 데이터 가공 ㄱ
         */
    }

    //아이디로 채팅방 찾기
    @Override
    public MessageRoom findByMessageRoomId(UUID messageRoomId){
        return messageRoomRepository.findById(messageRoomId)
                .orElseThrow(() -> new RestApiException(MessageErrorCode.NOT_FOUND_MESSAGE_ROOM));
    }

}
