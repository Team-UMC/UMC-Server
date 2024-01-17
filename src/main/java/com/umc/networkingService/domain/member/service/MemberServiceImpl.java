package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.member.client.AppleMemberClient;
import com.umc.networkingService.domain.member.client.KakaoMemberClient;
import com.umc.networkingService.domain.member.client.NaverMemberClient;
import com.umc.networkingService.domain.member.client.GoogleMemberClient;
import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.config.security.jwt.TokenInfo;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.global.common.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final KakaoMemberClient kakaoMemberClient;
    private final GoogleMemberClient googleMemberClient;
    private final NaverMemberClient naverMemberClient;
    private final AppleMemberClient appleMemberClient;
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

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
        return getNewToken(getMember.get());
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
        return getNewToken(getMember.get());
    }

    private MemberLoginResponse loginByNaver(final String accessToken){
        // naver 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = naverMemberClient.getnaverClientID(accessToken);
<<<<<<< HEAD
=======

>>>>>>> 96a6f7d2d1027e307fed110391f59bae07bbca09
        // 존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId,SocialType.NAVER);

        // 1. 없으면 (처음 로그인 하는 경우)
        if(getMember.isEmpty()) {
           return saveNewMember(clientId,SocialType.NAVER);
        }
        // 2. 있으면 (이미 로그인 했던 적이 있는 경우)
        return getNewToken(getMember.get());
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
        return getNewToken(getMember.get());
    }

    private MemberLoginResponse saveNewMember(String clientId, SocialType socialType) {
        Member member = memberMapper.toMember(clientId, socialType);
        Member newMember =  memberRepository.save(member);

        return getNewToken(newMember);
    }

    private MemberLoginResponse getNewToken(Member member) {
        // jwt 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getId());
        // refreshToken 디비에 저장
        refreshTokenService.saveTokenInfo(tokenInfo.getRefreshToken(), member.getId());

        return memberMapper.toLoginMember(member, tokenInfo);
    }
}