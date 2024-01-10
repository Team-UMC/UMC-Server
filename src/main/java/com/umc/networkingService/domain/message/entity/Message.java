package com.umc.networkingService.domain.message.entity;

import com.umc.networkingService.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Message extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name="message_id")
    private UUID id;

//    MessageRoom 테이블과 연결
//    @Column(nullable = false)
//    private UUID messageRoom;

    private String content;

    @Column(nullable = false)
    private Boolean isSender;

}
