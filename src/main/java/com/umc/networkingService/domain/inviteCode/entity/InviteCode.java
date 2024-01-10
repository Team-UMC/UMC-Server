package com.umc.networkingService.domain.inviteCode.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.BaseEntity;
import com.umc.networkingService.global.common.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InviteCode extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "invite_code_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String code;
}
