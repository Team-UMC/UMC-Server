package com.umc.networkingService.domain.branch.entity;

import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class BranchUniversity extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "branch_university_id")
    private UUID id;

    // 현재 기수인지 여부
    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private University university;
}
