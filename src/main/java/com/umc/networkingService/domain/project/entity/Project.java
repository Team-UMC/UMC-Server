package com.umc.networkingService.domain.project.entity;

import com.umc.networkingService.domain.project.dto.request.ProjectUpdateRequest;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
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
@DynamicInsert
public class Project extends BaseEntity {

    @Id
    @UuidGenerator
    private UUID id;

    private String logoImage;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProjectType> types = new ArrayList<>();

    @ColumnDefault("0")
    private Long hitCount;

    public void updateProject(ProjectUpdateRequest request){
        this.name = request.getName();
        this.description = request.getDescription();
        // 이전께 삭제 되는지 확인
        this.tags = request.getTags();
    }

    public void deleteProjectImage() {
        this.logoImage = null;
    }

    public void addHitCount() {
        this.hitCount += 1L;
    }
}
