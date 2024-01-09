package com.umc.networkingService.domain.board.entity;

import com.umc.networkingService.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    //member 테이블과 연결
    // private Member writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    //private List<String> images;

    //role enum과 연결
    //@Column(nullable = false)
    //private Role rolePermission;

    //part enum과 연결
    //@Column(nullable = false)
    //private Part partPermission;

    //semester enum과 연결
    //@Column(nullable = false)
    //private SemesterPermission;

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
