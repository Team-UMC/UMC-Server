package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardNoticePagingResponse;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.service.StaffBoardService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "운영진 게시판 API", description = "운영진용 게시판 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/staff/boards")
public class StaffBoardController {
    private final StaffBoardService staffBoardService;

    @Operation(summary = "공지사항 목록 조회/검색 API", description = "공지사항 목록을 조회합니다. keyword를 주면 검색됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD003", description = "해당 hostType의 공지사항을 볼 권한이 없을 경우 발생"),
    })
    @Parameters(value = {
            @Parameter(name = "hostType", description = "ALL, CENTER, BRANCH, CAMPUS 중 하나의 값을 대문자로 주세요."),
            @Parameter(name = "keyword", description = "keyword를 주지 않으면 모든 교내 공지사항 글이 조회됩니다. keyword를 주면 검색이 가능합니다."),
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다."),

    })
    @GetMapping("notices")
    public BaseResponse<BoardNoticePagingResponse> showNotices(@CurrentMember Member member,
                                                               @RequestParam(name = "host") HostType hostType,
                                                               @RequestParam(name = "keyword", required = false) String keyword,
                                                               @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC)
                                                               @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(staffBoardService.showNotices(member, hostType, keyword, pageable));
    }

    @Operation(summary = "교내 공지사항 핀 설정 API", description = "교내 공지사항 핀을 설정합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생"),
            @ApiResponse(responseCode = "BOARD003", description = "해당 공지사항 핀 설정 권한이 없을 경우 발생"),
    })
    @Parameters(value = {
            @Parameter(name = "boardId", description = "핀을 설정하고자 하는 boardId입니다."),
            @Parameter(name = "isPinned", description = "isPinned = true이면 핀으로 설정됩니다. false이면 핀 설정 해제됩니다.")
    })
    @PatchMapping("notices/{boardId}/pin")
    public BaseResponse<BoardIdResponse> toggleNoticePin(@CurrentMember Member member,
                                                         @PathVariable(value = "boardId") UUID boardId,
                                                         @RequestParam boolean isPinned) {
        return BaseResponse.onSuccess(staffBoardService.toggleNoticePin(member, boardId, isPinned));
    }
}
