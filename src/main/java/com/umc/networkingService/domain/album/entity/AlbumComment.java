package com.umc.networkingService.domain.album.entity;

import com.umc.networkingService.domain.album.dto.request.AlbumCommentUpdateRequest;
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
public class AlbumComment extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "album_comment_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "album_id")
    private Album album;

    //최상위 댓글인 경우 null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_comment_id")
    private AlbumComment parentComment;

    @Column(nullable = false)
    private String content;

    public void update(AlbumCommentUpdateRequest request) {
        this.content = request.getContent();
    }
}
