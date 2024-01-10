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
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ElementCollection(targetClass = Part.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "schedule_part_permission",
            joinColumns = @JoinColumn(name = "schedule_id")
    )
    @Column(name = "part", nullable = false)
    private List<Part> partPermission = new ArrayList<>();


    @ElementCollection(targetClass = Semester.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name= "schedule_semester_permission",
            joinColumns = @JoinColumn(name = "schedule_id")
    )
    @Column(name="semester",nullable = false)
    private List<Semester> semesterPermission = new ArrayList<>();


    @Column(nullable = false)
    private HostType hostType;
}
