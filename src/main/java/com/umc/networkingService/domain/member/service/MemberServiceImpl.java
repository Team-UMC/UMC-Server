package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.client.GoogleMemberClient;
import com.umc.networkingService.domain.member.client.KakaoMemberClient;
import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final KakaoMemberClient kakaoMemberClient;
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final GoogleMemberClient googleMemberClient;

    private final RefreshTokenService refreshTokenService;

    @Override
    public MemberLoginResponse socialLogin(String accessToken, SocialType socialType){
        // 로그인 구분
        if(socialType.equals(SocialType.KAKAO))
            return loginByKakao(accessToken);

        if(socialType.equals(SocialType.GOOGLE))
            return loginByGoogle(accessToken);

        return null;
    }

    private MemberLoginResponse loginByKakao(final String accessToken){
        // kakao 서버와 통신해서 유저 고유값(clientId) 받기
        String clientId = kakaoMemberClient.getkakaoClientID(accessToken);

        // 존재 여부 파악
        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId, SocialType.KAKAO);


        //1. 없으면 : Member 객체 생성하고 DB 저장
        if(getMember.isEmpty()){
            Member member = memberMapper.toMember(clientId, SocialType.KAKAO);
            Member newMember = memberRepository.save(member);

            // TODO: jwt 토큰 생성
            // TODO: refreshToken 디비에 저장

            return memberMapper.toLoginMember(newMember,"","");
        }

        // 2. 있으면 : 새로운 토큰 반환
        // TODO: 로직 생각해보기
        return null;

    }

    private MemberLoginResponse loginByGoogle(final String accessToken){
        String clientId = googleMemberClient.getgoogleClientID(accessToken);

        Optional<Member> getMember = memberRepository.findByClientIdAndSocialType(clientId, SocialType.GOOGLE);

        // 유저정보 없을때
        if(getMember.isEmpty()){
            Member member = memberMapper.toMember(clientId, SocialType.GOOGLE);
            Member newMember = memberRepository.save(member);

            //TODO : jwt 토큰 생성
            // TODO : refreshToken 디비에 저장

            return memberMapper.toLoginMember(newMember,"","");
        }

        // 유저정보 있을 때
        return memberMapper.toLoginMember(getMember.get(), "", "");
    }
}
