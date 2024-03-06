package com.umc.networkingService.domain.album.entity;

import com.umc.networkingService.domain.album.dto.request.AlbumUpdateRequest;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
@DynamicInsert
@Builder
@AllArgsConstructor
public class Album extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "album_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "member_id")
    private Member writer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ColumnDefault("0")
    private int hitCount;

    @ColumnDefault("0")
    private int heartCount;

    @ColumnDefault("0")
    private int commentCount;

    private boolean isFixed;

    public void updateAlbum(AlbumUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.semester = request.getSemester();
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void increaseHitCount() {
        this.hitCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

    public void setHeartCount(boolean isChecked) {
        if(isChecked)
            this.heartCount++;
        else
            this.heartCount--;
    }

    public void setIsFixed(boolean isFixed) {
        this.isFixed = isFixed;
    }
}
