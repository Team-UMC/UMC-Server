package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.member.entity.RefreshToken;
import com.umc.networkingService.domain.member.service.RefreshTokenService;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RefreshTokenServiceIntegrationTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Test
    void testSaveTokenInfo() {

        final String REFRESHTOKEN = "testSaveTokenInfo";
        final UUID MEMBERID = UUID.randomUUID();


        // When
        RefreshToken savedToken = refreshTokenService.saveTokenInfo(REFRESHTOKEN, MEMBERID);

        // Then (test에서 사용되는 assertion, 조건이 참이 아니라면 테스트 실패)
        assertNotNull(savedToken);
        assertEquals(REFRESHTOKEN, savedToken.getRefreshToken());
        assertEquals(MEMBERID, savedToken.getMemberId());
    }

    @Test
    void testFindByAccessToken() {

        final String REFRESHTOKEN = "testFindByAccessToken";
        final UUID MEMBERID = UUID.randomUUID();

        RefreshToken savedToken = refreshTokenService.saveTokenInfo(REFRESHTOKEN, MEMBERID);

        // When
        RefreshToken foundToken = refreshTokenService.findByMemberId(MEMBERID)
                .orElseThrow(() -> new RestApiException(ErrorCode.EXPIRED_MEMBER_JWT));

        // Then (test에서 사용되는 assertion, 조건이 참이 아니라면 테스트 실패)
        assertNotNull(foundToken);
        assertEquals(REFRESHTOKEN, foundToken.getRefreshToken());
        assertEquals(MEMBERID, foundToken.getMemberId());

    }

    @Test
    void testDeleteByAccessToken() {

        final String REFRESHTOKEN = "testDeleteByAccessToken";
        final UUID MEMBERID = UUID.randomUUID();

        RefreshToken savedToken = refreshTokenService.saveTokenInfo(REFRESHTOKEN, MEMBERID);

        RefreshToken tokenToDelete = refreshTokenService.findByMemberId(MEMBERID)
                .orElseThrow(() -> new RestApiException(ErrorCode.EXPIRED_MEMBER_JWT));


        // When
        refreshTokenService.delete(tokenToDelete);

        // Then (test에서 사용되는 assertion, 조건이 참이 아니라면 테스트 실패)
        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.findByMemberId(MEMBERID));

    }

    @Test
    void testGenerateAccessToken() {

        final UUID MEMBERID = UUID.randomUUID();

        Claims claims = Jwts.claims();
        claims.put("memberId", MEMBERID);

        String jwtToken = jwtTokenProvider.generateToken(MEMBERID).getAccessToken();

        // When
        UUID myMemberId = UUID.fromString(jwtTokenProvider.getMemberIdByToken(jwtToken));

        // Then (test에서 사용되는 assertion, 조건이 참이 아니라면 테스트 실패)
        assertEquals(MEMBERID, myMemberId);
    }

    @Test
    void testGenerateRefreshToken() {

        final UUID MEMBERID = UUID.randomUUID();

        Claims claims = Jwts.claims();
        claims.put("memberId", MEMBERID);

        String refreshToken = jwtTokenProvider.generateToken(MEMBERID).getRefreshToken();

        // When
        UUID myMemberId = UUID.fromString(jwtTokenProvider.getMemberIdByRefreshToken(refreshToken));

        // Then (test에서 사용되는 assertion, 조건이 참이 아니라면 테스트 실패)
        assertEquals(MEMBERID, myMemberId);
    }


}
