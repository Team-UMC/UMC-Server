package com.umc.networkingService.domain.member.entity;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
@DynamicInsert
public class Member extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "member_id")
    private UUID id;

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

    @Column(nullable = false)
    private SocialType socialType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberPosition> positions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @CollectionTable(name = "member_part", joinColumns = @JoinColumn(name = "member_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Part> parts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @CollectionTable(name = "member_semester", joinColumns = @JoinColumn(name = "member_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Semester> semesters = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    private String gitNickname;

    private  String notionLink;

    public void setMemberInfo(MemberSignUpRequest request, Role role, University university, Branch branch) {
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.role = role;
        this.university = university;
        this.branch = branch;
        this.parts.addAll(request.getParts());
        this.semesters.addAll(request.getSemesters());
    }

    public void updateMemberInfo(MemberUpdateMyProfileRequest request, String profileImage) {
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.statusMessage = request.getStatusMessage();
        if (profileImage != null) this.profileImage = profileImage;
    }

    public void updateMemberInfo(List<Part> parts, List<Semester> semesters) {
        this.parts.clear();
        this.parts = parts;
        this.semesters.clear();;
        this.semesters = semesters;
    }

    public void updatePositions(List<MemberPosition> memberPositions) {
        this.positions = memberPositions;
    }

    public void authenticationGithub(String gitNickname) {
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
