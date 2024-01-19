package com.umc.networkingService.domain.board.entity;

import com.umc.networkingService.domain.member.entity.Member;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
@DynamicInsert
public class Board extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "board_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "board_semester_permission", joinColumns = @JoinColumn(name = "board_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Semester> semesterPermission = new ArrayList<>();

    @Column(nullable = false)
    private HostType hostType;

    @Column(nullable = false)
    private BoardType boardType;

    @ColumnDefault("0")
    private int hitCount;

    @ColumnDefault("0")
    private int heartCount;

    @ColumnDefault("0")
    private int commentCount;

    private boolean isFixed; //notice가 아니면 null


}
