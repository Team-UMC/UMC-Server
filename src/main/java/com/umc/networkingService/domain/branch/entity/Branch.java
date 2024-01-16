package com.umc.networkingService.domain.branch.entity;

import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.Semester;
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
public class Branch extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "branch_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

}
