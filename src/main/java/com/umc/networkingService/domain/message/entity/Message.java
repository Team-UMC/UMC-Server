package com.umc.networkingService.domain.message.entity;

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
public class Message extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name="message_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private MessageRoom messageRoom;

    private String content;

    @Column(nullable = false)
    private Boolean isSender;

    public void updateContent(String content) {
        this.content = content;
    }

}
