package com.umc.networkingService.domain.friend.controller;

import com.umc.networkingService.domain.friend.dto.response.FriendIdResponse;
import com.umc.networkingService.domain.friend.service.FriendService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Friend 컨트롤러의")
public class FriendControllerTest extends ControllerTestConfig {

    @MockBean private FriendService friendService;

    @DisplayName("친구 추가 API 테스트")
    @Test
    public void createNewFriend() throws Exception {
        // given
        createMember("222222", Role.MEMBER);

        FriendIdResponse response = new FriendIdResponse(UUID.randomUUID());

        given(friendService.createNewFriend(any(), any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(post("/friends/" + response.getFriendId())
                        .header("Authorization", accessToken))
                .andDo(print()) // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.friendId").value(response.getFriendId().toString()));
    }
}
