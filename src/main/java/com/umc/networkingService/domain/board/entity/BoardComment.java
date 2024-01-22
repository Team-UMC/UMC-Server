package com.umc.networkingService.domain.board.entity;


import com.umc.networkingService.domain.board.dto.request.BoardCommentUpdateRequest;
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
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class BoardComment extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "board_comment_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name="board_id")
    private Board board;

    //최상위 댓글인 경우 null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_comment_id")
    private BoardComment parentComment;

    @Column(nullable = false)
    private String content;

    public void update(BoardCommentUpdateRequest request) {
        this.content = request.getContent();
    }

}
