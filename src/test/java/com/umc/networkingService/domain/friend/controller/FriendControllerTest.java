package com.umc.networkingService.domain.friend.controller;

import com.umc.networkingService.domain.friend.dto.response.FriendIdResponse;
import com.umc.networkingService.domain.friend.dto.response.FriendInquiryByStatusResponse;
import com.umc.networkingService.domain.friend.service.FriendService;
import com.umc.networkingService.domain.member.dto.SemesterPartInfo;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @DisplayName("친구 삭제 API 테스트")
    @Test
    public void deleteFriend() throws Exception {
        // given
        createMember("222222", Role.MEMBER);

        FriendIdResponse response = new FriendIdResponse(UUID.randomUUID());

        given(friendService.deleteFriend(any(), any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(delete("/friends/" + response.getFriendId())
                        .header("Authorization", accessToken))
                .andDo(print()) // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.friendId").value(response.getFriendId().toString()));

    }

    @DisplayName("접속/미접속 친구 조회 API 테스트")
    @Test
    public void inquiryFriendsByStatus() throws Exception {
        // given
        List<FriendInquiryByStatusResponse.FriendInfo> friends = List.of(
                new FriendInquiryByStatusResponse.FriendInfo(UUID.randomUUID(), "벡스", "김준석", null, "인하대학교", List.of("회장"), List.of(),
                        List.of(new SemesterPartInfo(Part.ANDROID, Semester.THIRD), new SemesterPartInfo(Part.SPRING, Semester.FOURTH), new SemesterPartInfo(Part.SPRING, Semester.FIFTH))),
                new FriendInquiryByStatusResponse.FriendInfo(UUID.randomUUID(), "하나", "심세원", null, "가천대학교", List.of(), List.of(),
                        List.of(new SemesterPartInfo(Part.ANDROID, Semester.FOURTH), new SemesterPartInfo(Part.SPRING, Semester.FIFTH))),
                new FriendInquiryByStatusResponse.FriendInfo(UUID.randomUUID(), "밈보", "김보민", null, "인하대학교", List.of("iOS 파트장"), List.of(),
                        List.of(new SemesterPartInfo(Part.IOS, Semester.FOURTH), new SemesterPartInfo(Part.SPRING, Semester.FIFTH)))
        );
        FriendInquiryByStatusResponse response = FriendInquiryByStatusResponse.builder()
                .friends(friends)
                .hasNext(false)
                .build();

        given(friendService.inquiryFriendsByStatus(any(), eq(true), any())).willReturn(response);
        given(memberRepository.findById(any(UUID.class))).willReturn(Optional.of(member));

        // when & then
        mockMvc.perform(get("/friends")
                        .param("size", "3")
                        .param("status", String.valueOf(true))
                        .header("Authorization", accessToken))
                .andDo(print()) // 응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.friends.size()").value(response.getFriends().size()));
    }
}
