package com.umc.networkingService.domain.member.entity;

import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class SemesterPart extends BaseEntity {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @Enumerated(EnumType.STRING)
    private Semester semester;

    @Enumerated(EnumType.STRING)
    private Part part;
}
