package com.umc.networkingService.domain.friend.entity;

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
