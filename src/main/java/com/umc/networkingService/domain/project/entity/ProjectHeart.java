package com.umc.networkingService.domain.project.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
@NoArgsConstructor
@SQLRestriction("deleted_at is null")
public class ProjectHeart extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "project_heart_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Project project;

    @Column(nullable = false)
    private boolean isChecked;
}
