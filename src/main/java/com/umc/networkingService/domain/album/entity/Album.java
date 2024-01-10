package com.umc.networkingService.domain.album.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.BaseEntity;
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

    //이미지 필수 저장
    @ElementCollection
    private List<String> images = new ArrayList<>();

    @ColumnDefault("0")
    private int hitCount;

    @ColumnDefault("0")
    private int heartCount;

    @ColumnDefault("0")
    private int commentCount;

}
