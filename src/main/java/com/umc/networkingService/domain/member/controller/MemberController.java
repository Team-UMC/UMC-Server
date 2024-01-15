package com.umc.networkingService.domain.member.controller;


import com.umc.networkingService.domain.member.dto.response.MemberLoginResponse;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    @PostMapping
    public MemberLoginResponse socialLogin(@RequestParam(value = "accessToken") String accessToken,
                                           @RequestParam(value = "socialType")SocialType socialType){
        return memberService.socialLogin(accessToken, socialType);
    }
}
