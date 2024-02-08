package com.umc.networkingService.config.security.jwt;

import com.umc.networkingService.config.security.auth.PrincipalDetails;
import com.umc.networkingService.config.security.auth.PrincipalDetailsService;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.AuthErrorCode;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.jwt-key}")
    private String jwtSecretKey;

    @Value("${jwt.refresh-key}")
    private String refreshSecretKey;

    private final PrincipalDetailsService principalDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "refreshToken";
    private static final long TOKEN_VALID_TIME = 1000 * 60L * 60L * 24L;  // 유효기간 1일
    private static final long REF_TOKEN_VALID_TIME = 1000 * 60L * 60L * 24L * 14L;  // 유효기간 14일

    @PostConstruct
    protected void init() {
        jwtSecretKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
    }

    public String generateAccessToken(UUID memberId) {
        Date now = new Date();
        Date accessTokenExpirationTime = new Date(now.getTime() + TOKEN_VALID_TIME);

        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(accessTokenExpirationTime)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    public String generateRefreshToken(UUID memberId) {
        Date now = new Date();
        Date refreshTokenExpirationTime = new Date(now.getTime() + REF_TOKEN_VALID_TIME);

        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(refreshTokenExpirationTime)
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();
    }

    public TokenInfo generateToken(UUID memberId) {

        String accessToken = generateAccessToken(memberId);
        String refreshToken = generateRefreshToken(memberId);

        return new TokenInfo(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        try {
            PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(
                    getMemberIdByToken(token));
            return new UsernamePasswordAuthenticationToken(principalDetails,
                    "", principalDetails.getAuthorities());
        } catch (UsernameNotFoundException exception) {
            throw new RestApiException(AuthErrorCode.UNSUPPORTED_JWT);
        }
    }

    public Authentication getRefreshAuthentication(String token) {
        try {
            PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(
                    getMemberIdByRefreshToken(token));
            return new UsernamePasswordAuthenticationToken(principalDetails,
                    "", principalDetails.getAuthorities());
        } catch (UsernameNotFoundException exception) {
            throw new RestApiException(AuthErrorCode.UNSUPPORTED_JWT);
        }
    }

    public String getMemberIdByToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).
                getBody().get("memberId").toString();
    }
    public String getMemberIdByRefreshToken(String token) {
        return Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(token).
                getBody().get("memberId").toString();
    }
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader(REFRESH_HEADER);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new RestApiException(AuthErrorCode.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new RestApiException(AuthErrorCode.EXPIRED_MEMBER_JWT);
        } catch (UnsupportedJwtException | SignatureException e) {
            throw new RestApiException(AuthErrorCode.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            throw new RestApiException(AuthErrorCode.EMPTY_JWT);
        }
    }
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new RestApiException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new RestApiException(AuthErrorCode.EXPIRED_MEMBER_JWT);
        } catch (UnsupportedJwtException | SignatureException e) {
            throw new RestApiException(AuthErrorCode.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            throw new RestApiException(AuthErrorCode.EMPTY_JWT);
        }
    }
}

