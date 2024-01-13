package com.umc.networkingService;

import com.umc.networkingService.domain.member.entity.RefreshToken;
import com.umc.networkingService.domain.member.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RefreshTokenServiceIntegrationTest {

    @Autowired
    private RefreshTokenService refreshTokenService;


    @Test
    void testSaveTokenInfo() {

        final UUID REFRESHTOKEN = UUID.randomUUID();  // 랜덤한 UUID 생성
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

        final UUID REFRESHTOKEN = UUID.randomUUID();  // 랜덤한 UUID 생성
        final UUID MEMBERID = UUID.randomUUID();

        RefreshToken savedToken = refreshTokenService.saveTokenInfo(REFRESHTOKEN, MEMBERID);

        // When
        RefreshToken foundToken = refreshTokenService.findByMemberId(MEMBERID);

        // Then (test에서 사용되는 assertion, 조건이 참이 아니라면 테스트 실패)
        assertNotNull(foundToken);
        assertEquals(REFRESHTOKEN, foundToken.getRefreshToken());
        assertEquals(MEMBERID, foundToken.getMemberId());

    }

    @Test
    void testDeleteByAccessToken() {

        final UUID REFRESHTOKEN = UUID.randomUUID();  // 랜덤한 UUID 생성
        final UUID MEMBERID = UUID.randomUUID();

        RefreshToken savedToken = refreshTokenService.saveTokenInfo(REFRESHTOKEN, MEMBERID);

        RefreshToken tokenToDelete = refreshTokenService.findByMemberId(MEMBERID);

        // When
        refreshTokenService.delete(tokenToDelete);

        // Then (test에서 사용되는 assertion, 조건이 참이 아니라면 테스트 실패)
        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.findByMemberId(MEMBERID));

    }
}
