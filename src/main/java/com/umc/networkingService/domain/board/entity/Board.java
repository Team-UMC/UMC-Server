package com.umc.networkingService.domain.board.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.BaseEntity;
import com.umc.networkingService.global.common.Part;
import com.umc.networkingService.global.common.Role;
import com.umc.networkingService.global.common.Semester;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ElementCollection
    private List<String> images = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role rolePermission;

    @ElementCollection(targetClass = Part.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "board_part_permission",
            joinColumns = @JoinColumn(name = "board_id")
    )
    @Column(name = "part", nullable = false)
    private List<Part> partPermission = new ArrayList<>();


    @ElementCollection(targetClass = Semester.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name= "board_semester_permission",
            joinColumns = @JoinColumn(name = "board_id")
    )
    @Column(name="semester",nullable = false)
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
