package com.umc.networkingService.domain.schedule.entity;

import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.BaseEntity;
import com.umc.networkingService.global.common.Part;
import com.umc.networkingService.global.common.Role;
import com.umc.networkingService.global.common.Semester;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Schedule extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "schedule_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String timeSetting;

    @Column(nullable = false)
    private String placeSetting;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role rolePermission;

    @Enumerated(EnumType.STRING)
    @JoinColumn
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Part> partPermission = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @JoinColumn
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Semester> semesterPermission = new ArrayList<>();

    @Column(nullable = false)
    private HostType hostType;
}
