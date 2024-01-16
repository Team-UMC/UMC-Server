package com.umc.networkingService.domain.member.entity;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Member extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "member_id")
    private UUID id;

    private String clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "university_id")
    private University university;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "branch_id")
    private Branch branch;

    private String profileImage;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long remainPoint;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String name;

    private String statusMessage;

    @Column(nullable = false)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_part", joinColumns = @JoinColumn(name = "member_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Part> part = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_semester", joinColumns = @JoinColumn(name = "member_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Semester> semester=new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Position position;

    private String gitNickname;

    private  String notionLink;
}
