package com.umc.networkingService.domain.friend.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.friend.dto.response.FriendIdResponse;
import com.umc.networkingService.domain.friend.service.FriendService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "친구 API", description = "친구 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    @Operation(summary = "친구 추가 API", description = "네이버, 카카오, 구글, 애플 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 멤버를 친구 추가한 경우 발생"),
            @ApiResponse(responseCode = "FRIEND001", description = "이미 친구 관계일 경우 발생")
    })
    @Parameter(name = "memberId", in = ParameterIn.PATH, required = true, description = "친구 추가할 멤버 id입니다.")
    @PostMapping("/{memberId}")
    public BaseResponse<FriendIdResponse> createNewFriend(@CurrentMember Member member,
                                                          @PathVariable UUID memberId) {
        return BaseResponse.onSuccess(friendService.createNewFriend(member, memberId));
    }
}
