package com.umc.networkingService.domain.member.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@Builder
@RedisHash(value = "jwtToken", timeToLive = 60*60*24*15) // 15일
public class RefreshToken { //redis에 저장할 객체

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 값이 생성
    private String id; // refreshToken 아이디

    private String refreshToken;

    @Indexed // 인덱스를 걸어주면 조회할 때 빠르게 찾을 수 있음
    private String accessToken;
    /*
    만료된 access Token으로 refresh Token을 찾아와서 유효성을 검사할 예정
     */
}

//만료된 access Token으로 refresh Token을 찾아와서 유효성을 검사한다.