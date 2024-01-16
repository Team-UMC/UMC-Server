package com.umc.networkingService.domain.project.entity;

import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Project extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "project_id")
    private UUID id;

    @Column(nullable = false)
    private String logoImage;

    @Column(nullable = false)
    private String name;

    private String slogan;

    private String description;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false)
    private Semester semester;

    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long hitCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long heartCount;
}
