package com.umc.networkingService;

import com.umc.networkingService.domain.member.entity.RefreshToken;
import com.umc.networkingService.domain.member.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class RefreshTokenServiceIntegrationTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Test
    void testSaveTokenInfo() {
        // Given
        String refreshToken = "sampleRefreshToken";
        String accessToken = "sampleAccessToken";

        // When
        RefreshToken savedToken = refreshTokenService.saveTokenInfo(refreshToken, accessToken);

        // Then (test에서 사용되는 assertion, 조건이 참이 아니라면 테스트 실패)
        assertNotNull(savedToken);
        assertEquals(refreshToken, savedToken.getRefreshToken());
        assertEquals(accessToken, savedToken.getAccessToken());
    }

    @Test
    void testFindByAccessToken() {
        // Given
        String refreshToken = "sampleRefreshToken";
        String accessToken = "sampleAccessToken";
        refreshTokenService.saveTokenInfo(refreshToken, accessToken);

        // When
        RefreshToken foundToken = refreshTokenService.findByAccessToken(accessToken);

        // Then (test에서 사용되는 assertion, 조건이 참이 아니라면 테스트 실패)
        assertNotNull(foundToken);
        assertEquals(refreshToken, foundToken.getRefreshToken());
        assertEquals(accessToken, foundToken.getAccessToken());

    }

    @Test
    void testDeleteByAccessToken() {
        // Given
        String refreshToken = "sampleRefreshToken";
        String accessToken = "sampleAccessToken";
        refreshTokenService.saveTokenInfo(refreshToken, accessToken);

        RefreshToken tokenToDelete = refreshTokenService.findByAccessToken(accessToken);

        // When
        refreshTokenService.deleteByAccessToken(tokenToDelete);

        // Then (test에서 사용되는 assertion, 조건이 참이 아니라면 테스트 실패)
        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.findByAccessToken(accessToken));

    }
}
