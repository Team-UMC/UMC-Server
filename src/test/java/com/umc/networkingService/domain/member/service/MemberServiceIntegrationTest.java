package com.umc.networkingService.domain.member.service;


import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.branch.repository.BranchUniversityRepository;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Member 서비스의 ")
@SpringBootTest
public class MemberServiceIntegrationTest {

    @Autowired private MemberService memberService;

    @Autowired private MemberRepository memberRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private BranchUniversityRepository branchUniversityRepository;

    private Member member;
    private University university;
    private Branch branch;
    private BranchUniversity branchUniversity;

    @BeforeEach
    public void setUp() {
        member = createMember();
        university = createUniversity();
        branch = createBranch();
        branchUniversity = createBranchUniversity();
    }

    private Member createMember() {
        return memberRepository.save(
                Member.builder()
                        .clientId("123456")
                        .socialType(SocialType.KAKAO)
                        .role(Role.MEMBER)
                        .build()
        );
    }

    private University createUniversity() {
        return universityRepository.save(
                University.builder()
                        .name("인하대학교")
                        .build()
        );
    }

    private Branch createBranch() {
        return branchRepository.save(
                Branch.builder()
                        .name("GACI")
                        .description("가치 지부입니다.")
                        .semester(Semester.FIFTH)
                        .build()
        );
    }

    private BranchUniversity createBranchUniversity() {
        return branchUniversityRepository.save(
                BranchUniversity.builder()
                        .branch(branch)
                        .university(university)
                        .isActive(Boolean.TRUE)
                        .build()
        );
    }

    @Test
    @DisplayName("회원 가입 테스트")
    @Transactional
    public void signUpTest() {
        // given
        MemberSignUpRequest request = MemberSignUpRequest.builder()
                .name("김준석")
                .nickname("벡스")
                .universityName("인하대학교")
                .parts(List.of(Part.SPRING))
                .semesters(List.of(Semester.THIRD, Semester.FOURTH, Semester.FIFTH))
                .campusPositions(List.of("회장"))
                .centerPositions(List.of("Server 파트장"))
                .build();

        // when
        memberService.signUp(member, request);

        // then
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        assertTrue(optionalMember.isPresent());
        Member savedMember = optionalMember.get();

        assertEquals("김준석", savedMember.getName());
        assertEquals("벡스", savedMember.getNickname());
        assertEquals("GACI", savedMember.getBranch().getName());
        assertEquals("인하대학교", savedMember.getUniversity().getName());
        assertEquals(1, savedMember.getPart().size());
        assertEquals(3, savedMember.getSemester().size());
    }
}