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

    @Column(nullable = false, unique = true)
    private String name;

    @ColumnDefault("0")
    private Long totalPoint;

    @ColumnDefault("1")
    private Integer currentLevel;

    //포인트 증가
    public void increasePoint(Long point) {
        this.totalPoint += point;
    }

    //정보 수정
    public void updateUniversity(String name, String universityLogo, String semesterLogo) {
        this.name = name;
        this.universityLogo = universityLogo;
        this.semesterLogo = semesterLogo;
    }

    //레벨 증가
    public void setLevel(Integer level) {
        this.currentLevel=level;
    }

    //마스코트 변경
    public void setMascot(Mascot mascot) {
        this.mascot = mascot;
    }


}
