package com.umc.networkingService.domain.branch.controller;

import com.umc.networkingService.domain.branch.converter.BranchConverter;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("Branch(지부) 컨트롤러의")
public class BranchControllerTest extends ControllerTestConfig {

    @Autowired
    private BranchConverter branchConverter;

    @MockBean
    private MemberService memberService;
}
