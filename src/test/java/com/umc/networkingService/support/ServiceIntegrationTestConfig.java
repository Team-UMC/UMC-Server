package com.umc.networkingService.support;

import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.mascot.entity.Mascot;
import com.umc.networkingService.domain.mascot.repository.MascotRepository;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.repository.SemesterPartRepository;
import com.umc.networkingService.domain.member.service.RefreshTokenService;
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
    @Autowired protected MascotRepository mascotRepository;

    @Autowired protected JwtTokenProvider jwtTokenProvider;
    @Autowired protected RefreshTokenService refreshTokenService;

    protected String refreshToken;
    protected University university;
    protected Branch branch;
    protected Mascot mascot;
    protected BranchUniversity branchUniversity;

    protected Member member;

    @BeforeEach
    public void setUp() {
        mascot = createMascot();
        university = createUniversity();
        branch = createBranch();
        branchUniversity = createBranchUniversity();
        member = createMember("111111", Role.MEMBER);
        setToken(member.getId());
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
                        .university(university)
                        .remainPoint(100L)
                        .build()
        );
    }

    protected MemberSignUpRequest getInfoRequest(String name, String nickname, List<String> campusPositions, List<String> centerPositions) {
        return MemberSignUpRequest.builder()
                .name(name)
                .nickname(nickname)
                .universityName("인하대학교")
                .semesterParts(memberMapper.toSemesterPartInfos(createSemesterPart(member)))
                .campusPositions(campusPositions)
                .centerPositions(centerPositions)
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


    protected Mascot createMascot() {
        return mascotRepository.save(
                Mascot.builder()
                        .dialogue("테스트")
                        .startLevel(1)
                        .endLevel(10)
                        .build()
        );
    }

    protected  University createUniversity() {
        return universityRepository.save(
                University.builder()
                        .name("인하대학교")
                        .totalPoint(0L)
                        .mascot(mascot)
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

    //새로운 브랜치 생성용
    protected Branch createBranch( String name ) {
        return branchRepository.save(
                Branch.builder()
                        .name(name)
                        .semester(Semester.FIFTH)
                        .build()
        );
    }

    //새로운 대학 생성용
    protected University createUniversity( String name ) {
        return universityRepository.save(
                University.builder()
                        .name(name)
                        .totalPoint(0L)
                        .mascot(mascot)
                        .build()
        );
    }
}
