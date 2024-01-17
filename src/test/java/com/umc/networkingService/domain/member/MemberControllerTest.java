package com.umc.networkingService.domain.member;

import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.dto.response.MemberSignUpResponse;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;



@DisplayName("Member 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private JwtTokenProvider jwtTokenProvider;

    @MockBean private MemberService memberService;

    @MockBean private MemberRepository memberRepository;

    private Member member;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        member = createMember();
        accessToken = jwtTokenProvider.generateToken(member.getId()).getAccessToken();
    }

    private Member createMember() {
        return Member.builder()
                .id(UUID.randomUUID())
                .clientId("123456")
                .socialType(SocialType.KAKAO)
                .role(Role.MEMBER)
                .build();
    }

    @Test
    @DisplayName("회원가입 API 테스트")
    public void signUpTest() throws Exception {
        // given
        MemberSignUpRequest request = MemberSignUpRequest.builder()
                .name("김준석")
                .nickname("벡스")
                .universityName("인하대학교")
                .parts(List.of(Part.SPRING))
                .semesters(List.of(Semester.THIRD, Semester.FOURTH, Semester.FIFTH))
                .campusPositions(List.of("회장"))
                .centerPositions(List.of("Server 파트장"))
                .build();

        MemberSignUpResponse response = new MemberSignUpResponse(member.getId());

        // when
        when(memberService.signUp(eq(member), any(MemberSignUpRequest.class))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));


        // then
        this.mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)  // accessToken 설정
                        .content(objectMapper.writeValueAsString(request)))
                        .andDo(print())  // 응답 출력
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.code").value("COMMON200"))
                        .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                        .andExpect(jsonPath("$.result").exists());  // result 필드가 존재하는지 검사


    }
}