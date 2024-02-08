package com.umc.networkingService.domain.mascot.entity;

import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Mascot extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name="mascot_id")
    private UUID id;

    @Column(nullable = false)
    private int startLevel;

    @Column(nullable = false)
    private int endLevel;

    @Column(nullable = false)
    private MascotType type;

    private String dialogue;

    private String image;
}
