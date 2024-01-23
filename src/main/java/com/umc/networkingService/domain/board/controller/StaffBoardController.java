package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.service.BoardService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name= "BOARD API", description = "운영진용 BOARD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/staff/boards")
public class StaffBoardController {
    private final BoardService boardService;

   /* @Operation(summary = "특정 게시글 상세 조회 API", description = "단일 게시글을 boardId를 통해 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생")

    })
    @GetMapping("notices")
    public BoardPagingResponse showAllNotices(@CurrentMember Member member,
                                              @PageableDefault(page = 1, sort = "created_at",
                                                direction = Sort.Direction.DESC) Pageable pageable) {
        return BaseResponse.onSuccess(boardService.showAllNotices(member, pageable));
    }*/
}
