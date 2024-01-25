package com.umc.networkingService.domain.university.entity;

import com.umc.networkingService.domain.mascot.entity.Mascot;
import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
@DynamicInsert
public class University extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name="university_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Mascot mascot;

    private String universityLogo;

    private String semesterLogo;

    @Column(nullable = false)
    private String name;

    @ColumnDefault("0")
    private Long totalPoint;
}
