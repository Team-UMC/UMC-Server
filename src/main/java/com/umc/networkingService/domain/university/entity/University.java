package com.umc.networkingService.domain.university.entity;

import com.umc.networkingService.domain.mascot.entity.Mascot;
import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
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
