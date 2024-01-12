package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.entity.RefreshToken;
import com.umc.networkingService.domain.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

        private final RefreshTokenRepository refreshTokenRepository; // Redis에 저장된 refreshToken을 가져오기 위해 DI

        @Transactional
        public RefreshToken saveTokenInfo(String refreshToken, String accessToken) { // Redis에 refreshToken 저장
            return refreshTokenRepository.save(
                    RefreshToken.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build()
            );
        }

        @Transactional
        public RefreshToken findByAccessToken(String accessToken) { // 만료된 accessToken으로 refreshToken을 찾아옴
            return refreshTokenRepository.findByAccessToken(accessToken).orElseThrow(() -> new IllegalArgumentException("Refresh Token이 존재하지 않습니다."));
        }

        @Transactional
        public void deleteByAccessToken(RefreshToken accessToken) { // Redis에 저장된 refreshToken 삭제
            refreshTokenRepository.findByAccessToken(accessToken.getAccessToken())
                    .ifPresent(refreshTokenRepository::delete);
        }
}

