package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.config.security.jwt.TokenInfo;
import com.umc.networkingService.domain.branch.service.BranchUniversityService;
import com.umc.networkingService.domain.member.client.AppleMemberClient;
import com.umc.networkingService.domain.member.client.GoogleMemberClient;
import com.umc.networkingService.domain.member.client.KakaoMemberClient;
import com.umc.networkingService.domain.member.client.NaverMemberClient;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberGenerateTokenResponse;
import com.umc.networkingService.domain.member.dto.response.MemberIdResponse;
import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.RefreshToken;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.service.UniversityService;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberService memberService;

    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    private final SemesterPartService semesterPartService;
    private final MemberPositionService memberPositionService;
    private final RefreshTokenService refreshTokenService;
    private final UniversityService universityService;
    private final BranchUniversityService branchUniversityService;

    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoMemberClient kakaoMemberClient;
    private final GoogleMemberClient googleMemberClient;
    private final NaverMemberClient naverMemberClient;
    private final AppleMemberClient appleMemberClient;

    // 소셜 로그인을 수행하는 함수
    @Override
    public MemberLoginResponse socialLogin(String accessToken, SocialType socialType){
        // 로그인 구분
        if(socialType.equals(SocialType.KAKAO))
            return loginByKakao(accessToken);

        if(socialType.equals(SocialType.GOOGLE))
            return loginByGoogle(accessToken);

        if(socialType.equals(SocialType.NAVER))
            return loginByNaver(accessToken);

        if(socialType.equals(SocialType.APPLE))
            return loginByApple(accessToken);

        return null;
    }

    // 회원가입을 수행하는 함수
    @Override
    @Transactional
    public MemberIdResponse signUp(Member loginMember, MemberSignUpRequest request) {

        Member member = loadEntity(loginMember.getId());

        // 소속 대학교 탐색
        University university = universityService.findUniversityByName(request.getUniversityName());

        // 멤버 직책 저장
        memberPositionService.saveMemberPositionInfos(member, request.getCampusPositions(), request.getCenterPositions());

        // 기수별 파트 저장
        semesterPartService.saveSemesterPartInfos(member, request.getSemesterParts());

        // 이외의 기본 정보 저장
        member.setMemberInfo(request.getName(), request.getNickname(), university,
                // 대학교와 멤버의 마지막 기수를 통해 지부 정보 조회
                branchUniversityService.findBranchByUniversityAndSemester(university, member.getLatestSemesterPart().getSemester()));

        return new MemberIdResponse(memberService.saveEntity(member).getId());
    }

    // 새로운 액세스 토큰 발급 함수
    @Override
    @Transactional
    public MemberGenerateTokenResponse generateNewAccessToken(String refreshToken, Member loginMember) {

        Member member = loadEntity(loginMember.getId());

        RefreshToken savedRefreshToken = refreshTokenService.findByMemberId(member.getId())
                .orElseThrow(() -> new RestApiException(ErrorCode.EXPIRED_MEMBER_JWT));

        // 디비에 저장된 refreshToken과 동일하지 않다면 유효하지 않음
        if (!refreshToken.equals(savedRefreshToken.getRefreshToken()))
            throw new RestApiException(ErrorCode.INVALID_REFRESH_TOKEN);

        return new MemberGenerateTokenResponse(jwtTokenProvider.generateAccessToken(member.getId()));
    }

    // 로그아웃 함수
    @Override
    @Transactional
    public MemberIdResponse logout(Member loginMember) {
        Member member = loadEntity(loginMember.getId());

        deleteRefreshToken(member);
        return new MemberIdResponse(member.getId());
    }

    // 회원 탈퇴 함수
    @Override
    @Transactional
    public MemberIdResponse withdrawal(Member loginMember) {
        // 멤버 soft delete
        Member member = loadEntity(loginMember.getId());

        // refreshToken 삭제
        deleteRefreshToken(member);

        // 멤버 soft delete
        member.delete();

        return new MemberIdResponse(member.getId());
    }

    private MemberLoginResponse loginByApple(final String accessToken){
        // apple 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = appleMemberClient.getappleClientID(accessToken);
        //존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId, SocialType.APPLE);

        //1. 없으면 : Member 객체 생성하고 DB 저장
        if(getMember.isEmpty()){
            return saveNewMember(clientId, SocialType.APPLE);
        }
        // 2. 있으면 : 새로운 토큰 반환
        boolean isServiceMember = getMember.get().getName() != null;
        return getNewToken(getMember.get(), isServiceMember);
    }

    private MemberLoginResponse loginByKakao(final String accessToken){
        // kakao 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = kakaoMemberClient.getkakaoClientID(accessToken);
        // 존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId, SocialType.KAKAO);

        // 1. 없으면 : Member 객체 생성하고 DB 저장
        if(getMember.isEmpty()) {
            return saveNewMember(clientId, SocialType.KAKAO);
        }
        // 2. 있으면 : 새로운 토큰 반환
        boolean isServiceMember = getMember.get().getName() != null;
        return getNewToken(getMember.get(), isServiceMember);
    }

    private MemberLoginResponse loginByNaver(final String accessToken){
        // naver 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = naverMemberClient.getnaverClientID(accessToken);
        // 존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId,SocialType.NAVER);

        // 1. 없으면 (처음 로그인 하는 경우)
        if(getMember.isEmpty()) {
            return saveNewMember(clientId,SocialType.NAVER);
        }
        // 2. 있으면 (이미 로그인 했던 적이 있는 경우)
        boolean isServiceMember = getMember.get().getName() != null;

        return getNewToken(getMember.get(), isServiceMember);
    }

    private MemberLoginResponse loginByGoogle(final String accessToken){
        // google 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = googleMemberClient.getgoogleClientID(accessToken);
        // 존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId, SocialType.GOOGLE);

        // 1. 없으면 : Member 객체 생성하고 DB 저장
        if(getMember.isEmpty()){
            return saveNewMember(clientId, SocialType.GOOGLE);
        }
        // 2. 있으면 : 새로운 토큰 반환
        boolean isServiceMember = getMember.get().getName() != null;
        return getNewToken(getMember.get(), isServiceMember);
    }

    private MemberLoginResponse saveNewMember(String clientId, SocialType socialType) {
        Member member = memberMapper.toMember(clientId, socialType);
        Member newMember =  memberRepository.save(member);

        return getNewToken(newMember, false);
    }

    private MemberLoginResponse getNewToken(Member member, boolean isServiceMember) {
        // jwt 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getId());
        // refreshToken 디비에 저장
        refreshTokenService.saveTokenInfo(tokenInfo.getRefreshToken(), member.getId());

        return memberMapper.toLoginMember(member, tokenInfo, isServiceMember);
    }

    // member 객체를 이용한 refreshToken 삭제 함수
    private void deleteRefreshToken(Member member) {
        Optional<RefreshToken> refreshToken = refreshTokenService.findByMemberId(member.getId());

        refreshToken.ifPresent(refreshTokenService::delete);
    }

    // 멤버 로드 함수
    @Override
    public Member loadEntity(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_MEMBER));
    }
}
