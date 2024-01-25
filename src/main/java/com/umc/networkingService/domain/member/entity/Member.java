package com.umc.networkingService.domain.member.entity;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("deleted_at is null")
@DynamicInsert
public class Member extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "member_id")
    private UUID id;

    @Column(nullable = false)
    private String clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private String profileImage;

    @ColumnDefault("0")
    private Long remainPoint;

    @ColumnDefault("0")
    private Long contributionPoint;

    private String nickname;

    private String name;

    private String statusMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<SemesterPart> semesterParts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberPosition> positions = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    private Role role;

    private String gitNickname;

    private String notionLink;
    
    private LocalDateTime lastActiveTime;

    public void setMemberInfo(String name, String nickname, University university, Branch branch) {
        this.name = name;
        this.nickname = nickname;
        this.university = university;
        this.branch = branch;
    }

    public void updateMemberInfo(MemberUpdateMyProfileRequest request, String profileImage) {
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.statusMessage = request.getStatusMessage();
        this.profileImage = profileImage;
    }

    public void updatePositions(List<MemberPosition> memberPositions) {
        this.positions = memberPositions;
    }

    public void updateSemesterParts(List<SemesterPart> semesterParts) {
        this.semesterParts = semesterParts;
    }
    public void authenticateGithub(String gitNickname) {
        this.gitNickname = gitNickname;
    }

    public void updateContributionPoint(Long usedPoint) {
        if (this.contributionPoint == null) this.contributionPoint = usedPoint;
        else this.contributionPoint += usedPoint;
    }

    public void updateRole(Role role) {
        this.role = role;
    }
}
