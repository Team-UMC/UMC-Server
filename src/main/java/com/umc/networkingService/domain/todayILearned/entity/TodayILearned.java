package com.umc.networkingService.domain.todayILearned.entity;

import com.umc.networkingService.global.common.BaseEntity;
import com.umc.networkingService.global.common.Part;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.hibernate.annotations.GenericGenerator;

public class TodayILearned extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private UUID writer;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    private String content;

    @Column(nullable = false)
    private List<Part> part;

    @Column(nullable = false)
    private Boolean isNotion;

    @Column(nullable = false)
    @Builder.Default
    private List<String> attechedFilees = new ArrayList<>();
}
