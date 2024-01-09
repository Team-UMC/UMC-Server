package com.umc.networkingService.domain.project.entity;

import com.umc.networkingService.global.common.BaseEntity;
import com.umc.networkingService.global.common.Semester;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

@Getter
@RequiredArgsConstructor
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String logoImage;

    @Column(nullable = false)
    private String name;

    private String slogan;

    private String description;

    @Column(nullable = false)
    @Builder.Default
    private List<String> tags = new ArrayList<>();

//    Semester 테이블과 연결
//    @Column(nullable = false)
//    private Semester semester;

    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long hitCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long heartCount;
}
