package com.umc.networkingService.domain.test.service;

import com.umc.networkingService.config.initial.BranchInfo;
import com.umc.networkingService.config.initial.UniversityInfo;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.service.BranchService;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.SemesterPartRepository;
import com.umc.networkingService.domain.member.service.AuthService;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.service.UniversityService;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestService {
    private final MemberService memberService;
    private final AuthService authService;
    private final SemesterPartRepository semesterPartRepository;
    private final UniversityService universityService;
    private final BranchService branchService;
    private final MemberMapper memberMapper;


    public void CheckFlag(Integer flag) {
        if (flag == 1)
            throw new RestApiException(GlobalErrorCode.TEMP_EXCEPTION);
    }

    @Transactional
    public String createDummyBoard(Member member, int size) {
        generateDummyMember(member);

        return "더미데이터 생성 완료";
    }

    @Transactional
    public List<Member> generateDummyMember(Member loginMember) {
        List<Member> members = new ArrayList<>();
        List<University> universities = new ArrayList<>();
        Branch branch = branchService.loadEntity(loginMember.getBranch().getId());

        //로그인한 멤버의 지부에 해당하는 university list를 불러오기
        List<UniversityInfo> univInfos = BranchInfo.getBranchInfo(branch.getName()).getUniversities();
        for (UniversityInfo univInfo : univInfos)
            universities.add(universityService.findUniversityByName(univInfo.getName()));

        Random random = new Random();

        for (MemberDummyInfo memberInfo : MemberDummyInfo.values()) {

            int randomIndex = random.nextInt(universities.size());
            University randomUniv = universities.get(randomIndex);

            Member member = memberService.loadEntity(authService.saveNewDummyMember(UUID.randomUUID().toString()).getMemberId());


            List<String> campusPositions = new ArrayList<>();
            if(!memberInfo.getRole().equals("챌린저"))
                campusPositions.add(memberInfo.getRole());
            MemberSignUpRequest request = MemberSignUpRequest.builder()
                    .name(memberInfo.getName())
                    .nickname(memberInfo.getNickname())
                    .universityName(randomUniv.getName())
                    .semesterParts(memberMapper.toSemesterPartInfos(createSemesterPart(member,memberInfo)))
                    .campusPositions(campusPositions)
                    .centerPositions(new ArrayList<>())
                    .build();
            authService.signUp(member, request);
            members.add(member);
        }
        return members;
    }

    public List<SemesterPart> createSemesterPart(Member member, MemberDummyInfo memberInfo) {
        List<SemesterPart> semesterParts = new ArrayList<>();
        for (int i = 0; i < memberInfo.getSemesters().size(); i++)
            semesterParts.add(SemesterPart.builder().member(member).part(memberInfo.getParts().get(i))
                    .semester(memberInfo.getSemesters().get(i)).build());

        return semesterPartRepository.saveAll(semesterParts);
    }

}
