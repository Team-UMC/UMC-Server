package com.umc.networkingService.domain.board.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.Semester;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
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

    @Builder
    public Board(Member member, String title, String content, HostType hostType, BoardType boardType) {
        this.writer = member;
        this.title = title;
        this.content = content;
        this.hostType = hostType;
        this.boardType = boardType;
    }
    public static Board newBoard(Member member, String title, String content, HostType hostType, BoardType boardType) {
        return Board.builder()
                .member(member)
                .title(title)
                .content(content)
                .hostType(hostType)
                .boardType(boardType).build();
    }
}
