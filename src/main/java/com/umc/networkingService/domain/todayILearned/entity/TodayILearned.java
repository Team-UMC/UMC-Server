package com.umc.networkingService.domain.todayILearned.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.BaseEntity;
import com.umc.networkingService.global.common.Part;
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
@Table(name = "today_i_learned")
public class TodayILearned extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "today_i_learned_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member writer;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Part part;

    private Boolean linkedNotion;
}
