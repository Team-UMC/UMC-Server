package com.umc.networkingService.config;

import com.umc.networkingService.domain.member.interceptor.LastActiveInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LastActiveInterceptor lastActiveInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(lastActiveInterceptor)
                .addPathPatterns("/**") // 모든 요청에 대해 인터셉터 적용
                .excludePathPatterns("/v3/**", "/swagger-ui/**") // 스웨거 요청은 제외
                .excludePathPatterns("/members/login"); // 로그인 API 제외
    }
}
