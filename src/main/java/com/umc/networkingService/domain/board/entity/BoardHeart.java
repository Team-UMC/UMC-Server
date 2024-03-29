package com.umc.networkingService.domain.board.entity;


import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class BoardHeart extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "board_heart_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "board_id")
    private Board board;

    private boolean isChecked;

    public void setBoard(Board board) {
        this.board = board;
    }

    public void toggleHeart() {
        this.isChecked = !this.isChecked;
    }

}
