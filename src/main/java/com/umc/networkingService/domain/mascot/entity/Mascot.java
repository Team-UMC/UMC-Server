package com.umc.networkingService.domain.mascot.entity;

import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MascotType type;

    @ElementCollection
    private List<String> dialogues;

    private String image;

    // 대사 2개 랜덤 추출 함수
    public List<String> getRandomDialogues() {
        List<String> dialogues = this.getDialogues();

        // 순서 섞어서 랜덤 2개 추출
        Collections.shuffle(dialogues, new Random());
        return dialogues.stream().limit(2).toList();
    }
}
