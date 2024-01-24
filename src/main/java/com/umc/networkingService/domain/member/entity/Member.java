package com.umc.networkingService.domain.member.entity;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;
import java.util.stream.Collectors;

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

    private  String notionLink;

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

    public Semester getRecentSemester() {
        List<SemesterPart> semesterParts = this.getSemesterParts();

        return semesterParts.stream()
                .map(SemesterPart::getSemester)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElseThrow(()-> new RestApiException(ErrorCode.EMPTY_SEMESTER_PART));
    }

    public Part getRecentPart() {
        Semester recentSemester = getRecentSemester();
        List<SemesterPart> semesterParts = this.getSemesterParts();

        // 최신 Semester에 해당하는 SemesterPart 찾기
        Optional<SemesterPart> recentSemesterPart = semesterParts.stream()
                .filter(part -> part.getSemester() == recentSemester)
                .findFirst();

        return recentSemesterPart.map(SemesterPart::getPart)
                .orElseThrow(()-> new RestApiException(ErrorCode.EMPTY_SEMESTER_PART));
    }

    public List<Semester> getSemesters() {
        return this.getSemesterParts().stream()
                .map(SemesterPart::getSemester)
                .collect(Collectors.toList());
    }

    // 테스트 코드용
    public void updateRole(Role role) {
        this.role = role;
    }
}
