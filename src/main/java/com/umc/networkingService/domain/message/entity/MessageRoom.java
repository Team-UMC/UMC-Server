package com.umc.networkingService.domain.message.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
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
