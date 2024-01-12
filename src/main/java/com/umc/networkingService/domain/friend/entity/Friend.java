package com.umc.networkingService.domain.friend.entity;

import com.umc.networkingService.domain.member.entity.Member;
import jakarta.persistence.*;
import com.umc.networkingService.global.common.base.BaseEntity;

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
public class Friend extends BaseEntity  {

    @Id
    @UuidGenerator
    @Column(name = "friend_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member receiver;
}
