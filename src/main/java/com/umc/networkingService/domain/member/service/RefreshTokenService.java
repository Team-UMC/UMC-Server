package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.entity.RefreshToken;
import com.umc.networkingService.domain.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

        private final RefreshTokenRepository refreshTokenRepository; // Redis에 저장된 refreshToken을 가져오기 위해 DI

        @Transactional
        public RefreshToken saveTokenInfo(String refreshToken, UUID memberId) { // Redis에 refreshToken 저장
            return refreshTokenRepository.save(
                    RefreshToken.builder()
                            .memberId(memberId)
                            .refreshToken(refreshToken)
                            .build()
            );
        }

        @Transactional
        public Optional<RefreshToken> findByMemberId(UUID memberId) { // 만료된 accessToken으로 refreshToken을 찾아옴
            return refreshTokenRepository.findByMemberId(memberId);
        }

        @Transactional
        public void delete(RefreshToken refreshToken) { // Redis에 저장된 refreshToken 삭제
            // Redis에 저장된 refreshToken 삭제
            refreshTokenRepository.delete(refreshToken);
        }
}

