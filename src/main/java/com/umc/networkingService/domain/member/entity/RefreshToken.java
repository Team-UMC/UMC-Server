package com.umc.networkingService.domain.member.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
@RedisHash(value = "jwtToken", timeToLive = 60*60*24*30) // 30일
public class RefreshToken { //redis에 저장할 객체

    @Id
    @UuidGenerator
    private UUID refreshTokenId; // refreshToken 아이디

    private UUID refreshToken;

    @Indexed // 인덱스를 걸어주면 조회할 때 빠르게 찾을 수 있음
    private UUID memberId;
    /*
    만료된 access Token을 가진 memberId 값으로  refresh Token을 찾아와서 유효성을 검사할 예정
     */
}

//memberId 값으로 refresh Token을 찾아와서 유효성을 검사한다.