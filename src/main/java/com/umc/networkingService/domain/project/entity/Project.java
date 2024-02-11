package com.umc.networkingService.domain.project.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Project extends BaseEntity {

    @Id
    @UuidGenerator
    private UUID id;

    private String logoImage;

    @Column(nullable = false, unique = true)
    private String name;

    private String slogan;

    private String description;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false)
    private Semester semester;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Type> type = new ArrayList<>();

    @ColumnDefault("0")
    private Long hitCount;

    @ColumnDefault("0")
    private Long heartCount;

    public void updateProject(String name, String slogan){
        this.name = name;
        this.slogan = slogan;
    }
}
