package com.umc.networkingService.domain.messageRoom.entity;

import com.umc.networkingService.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class MessageRoom extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name="message_room_id")
    private UUID id;

//    member 테이블과 연결
//    @Column(nullable = false)
//    private UUID sender;

//    member 테이블과 연결
//    @Column(nullable = false)
//    private UUID receiver;

    @Column(nullable = false)
    private Boolean isAnonymous;
}
