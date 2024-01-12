package com.umc.networkingService.domain.branch.entity;

import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class BranchUniversity extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "branch_university_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private University university;
}
