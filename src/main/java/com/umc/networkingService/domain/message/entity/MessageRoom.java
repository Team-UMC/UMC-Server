package com.umc.networkingService.domain.message.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class MessageRoom extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name="message_room_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Column(nullable = false)
    private Boolean isAnonymous;
}

/*
1. 쪽지를 보내기 시작할 떄, 처음 보낸 사람이 익명이라면, 받는 사람에게는 보낸 사람이 익명으로 뜸
2. 익명으로 보낸 사람 입장에서는 받는 사람이 실명으로 뜸
3. 그럼,A 입장에서는 A가 B에게 익명으로 쪽지를 보낸 채팅방, A와 B가 실명으로 쪽지를 주고받는 채팅방이 따로 생길 수도 있음
4. A가 B에게 익명인 채팅방은 하나만 생성됨 (A가 sender 일떄, isAnonymous = true) isAnonymous = true -> receiver 입장에서 sender가 익명
5. A가 B에게 실명인 채팅방은 하나만 생성됨 (A가 sender 일떄, isAnonymous = false)
6. A가 receiver 일떄, isAnonymous = false인 채팅방이 2개가 생김
-> 필드 바꿔야함
(그럼 A가 )
*/