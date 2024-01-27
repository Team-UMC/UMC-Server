package com.umc.networkingService.domain.member.interceptor;

import com.umc.networkingService.config.security.auth.PrincipalDetails;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LastActiveInterceptor implements HandlerInterceptor {
    @Autowired
    private MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        memberService.updateMemberActiveTime(member.getId());

        return true;
    }
}
