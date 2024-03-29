package com.umc.networkingService.domain.schedule.entity;

import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest.UpdateSchedule;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
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
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private String placeSetting;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "schedule_semester_permission", joinColumns = @JoinColumn(name = "schedule_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Semester> semesterPermission = new ArrayList<>();

    @Column(nullable = false)
    private HostType hostType;

    public void updateSchedule(UpdateSchedule request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.startDateTime = request.getStartDateTime();
        this.endDateTime = request.getEndDateTime();
        this.semesterPermission = request.getSemesterPermission();
        this.hostType = request.getHostType();
        this.placeSetting = request.getPlaceSetting();
    }
}