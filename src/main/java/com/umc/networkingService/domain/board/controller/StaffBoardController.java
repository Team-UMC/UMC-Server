package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardNoticePagingResponse;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.service.StaffBoardService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "STAFF BOARD API", description = "운영진용 BOARD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/staff/boards")
public class StaffBoardController {
    private final StaffBoardService staffBoardService;

    @Operation(summary = "교내 공지사항 목록 조회/검색 API", description = "keyword를 주지 않으면  모든 교내 공지사항 글이 조회됩니다. keyword를 주면 검색이 가능합니다." +
            " page 시작은 0번부터, 내림차순으로 조회됩니다. hostType은 ALL, CENTER, BRANCH, CAMPUS 중 하나를 주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping("notices")
    public BaseResponse<BoardNoticePagingResponse> showNotices(@CurrentMember Member member,
                                                               @RequestParam(name = "hostType") HostType hostType,
                                                               @RequestParam(name = "keyword", required = false) String keyword,
                                                               @PageableDefault(sort = "created_at",
                                                                       direction = Sort.Direction.DESC) Pageable pageable) {
        return BaseResponse.onSuccess(staffBoardService.showNotices(member, hostType, keyword, pageable));
    }

    @Operation(summary = "교내 공지사항 핀 설정 API", description = "교내 공지사항 핀을 설정합니다. isPinned = true이면 핀으로 설정됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생")

    })
    @PatchMapping("notices/{boardId}/pin")
    public BaseResponse<BoardIdResponse> toggleNoticePin(@CurrentMember Member member,
                                                         @PathVariable(value = "boardId") UUID boardId,
                                                         @RequestParam boolean isPinned) {
        return BaseResponse.onSuccess(staffBoardService.toggleNoticePin(member, boardId, isPinned));
    }
}
