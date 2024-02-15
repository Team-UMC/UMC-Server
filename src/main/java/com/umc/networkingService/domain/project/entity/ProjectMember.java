package com.umc.networkingService.domain.project.entity;

import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Part;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("deleted_at is null")
public class ProjectMember extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "project_member_id")
    private UUID id;

    private String nickname;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Project project;

    @Column(nullable = false)
    private Part part;
}
