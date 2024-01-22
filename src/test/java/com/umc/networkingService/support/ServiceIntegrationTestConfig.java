package com.umc.networkingService.support;

import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.service.RefreshTokenService;
import com.umc.networkingService.domain.member.repository.SemesterPartRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public abstract class ServiceIntegrationTestConfig {

    @Autowired protected MemberMapper memberMapper;
    @Autowired protected MemberRepository memberRepository;
    @Autowired protected SemesterPartRepository semesterPartRepository;
    @Autowired protected UniversityRepository universityRepository;
    @Autowired protected BranchRepository branchRepository;
    @Autowired protected BranchUniversityRepository branchUniversityRepository;

    @Autowired protected JwtTokenProvider jwtTokenProvider;
    @Autowired protected RefreshTokenService refreshTokenService;

    protected String refreshToken;
    protected University university;
    protected Branch branch;
    protected BranchUniversity branchUniversity;
    protected Member member;

    @BeforeEach
    public void setUp() {
        member = createMember("111111", Role.MEMBER);
        setToken(member.getId());
        university = createUniversity();
        branch = createBranch();
        branchUniversity = createBranchUniversity();
    }

    @AfterEach
    public void tearDown() {
        memberRepository.deleteAll();
        branchUniversityRepository.deleteAll();
        universityRepository.deleteAll();
        branchRepository.deleteAll();
    }

    protected Member createMember(String clientId, Role role) {
        return memberRepository.save(
                Member.builder()
                        .clientId(clientId)
                        .socialType(SocialType.KAKAO)
                        .role(role)
                        .build()
        );
    }

    protected MemberSignUpRequest getInfoRequest(Member nowMember) {
        return MemberSignUpRequest.builder()
                .name("김준석")
                .nickname("벡스")
                .universityName("인하대학교")
                .semesterParts(memberMapper.toSemesterPartInfos(createSemesterPart(member)))
                .campusPositions(List.of("Android 파트장"))
                .centerPositions(List.of())
                .build();
    }

    protected List<SemesterPart> createSemesterPart(Member member) {
        List<SemesterPart> semesterParts = List.of(
                SemesterPart.builder().member(member).part(Part.ANDROID).semester(Semester.THIRD).build(),
                SemesterPart.builder().member(member).part(Part.SPRING).semester(Semester.FOURTH).build()
        );

        return semesterPartRepository.saveAll(semesterParts);
    }

    protected void setToken(UUID memberId) {
        refreshToken = jwtTokenProvider.generateRefreshToken(memberId);
        refreshTokenService.saveTokenInfo(refreshToken, memberId);
    }

    protected  University createUniversity() {
        return universityRepository.save(
                University.builder()
                        .name("인하대학교")
                        .build()
        );
    }

    protected Branch createBranch() {
        return branchRepository.save(
                Branch.builder()
                        .name("GACI")
                        .description("가치 지부입니다.")
                        .semester(Semester.FIFTH)
                        .build()
        );
    }

    protected BranchUniversity createBranchUniversity() {
        return branchUniversityRepository.save(
                BranchUniversity.builder()
                        .branch(branch)
                        .university(university)
                        .isActive(Boolean.TRUE)
                        .build()
        );
    }
}
