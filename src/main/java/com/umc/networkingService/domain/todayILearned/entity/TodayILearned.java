package com.umc.networkingService.domain.todayILearned.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.BaseEntity;
import com.umc.networkingService.global.common.Part;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class TodayILearned extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "til_id")
    private UUID id;


    //private Member writer;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Part part;

    @Column(nullable = false)
    private Boolean isNotion;


    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "til_files", joinColumns = @JoinColumn(name = "til_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> attechedFilees = new ArrayList<>();
}
