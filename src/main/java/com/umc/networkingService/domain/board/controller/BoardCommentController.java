package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.board.dto.request.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.request.BoardCommentUpdateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardCommentPagingResponse;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.service.BoardCommentService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Board Comment API", description = "게시판 댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/comments")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @Operation(summary = "댓글 작성 API", description = "댓글을 작성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생")
    })
    @PostMapping
    public BaseResponse<BoardCommentIdResponse> addBoardComment(@CurrentMember Member member,
                                                                @Valid @RequestPart("request") BoardCommentAddRequest request) {
        return BaseResponse.onSuccess(boardCommentService.addBoardComment(member, request));
    }

    @Operation(summary = "댓글 수정 API", description = "댓글을 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMENT001", description = "댓글을 찾을 수 없을 경우 발생")
    })
    @PatchMapping("/commentId")
    public BaseResponse<BoardCommentIdResponse> updateBoardComment(@CurrentMember Member member,
                                                                @PathVariable(value="commentId") UUID commentId,
                                                                @Valid @RequestPart("request") BoardCommentUpdateRequest request) {
        return BaseResponse.onSuccess(boardCommentService.updateBoardComment(member, commentId, request));
    }


    @Operation(summary = "댓글 삭제 API", description = "댓글을 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMENT001", description = "댓글을 찾을 수 없을 경우 발생")
    })
    @DeleteMapping("/commentId")
    public BaseResponse<BoardCommentIdResponse> deleteBoardComment(@CurrentMember Member member,
                                                                   @PathVariable(value = "commentId") UUID commentId) {
        return BaseResponse.onSuccess(boardCommentService.deleteBoardComment(member, commentId));
    }

    @Operation(summary = " 특정 게시글 댓글 목록 조회 API", description = "특정 게시글의 댓글 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping("/{boardId}")
    public BaseResponse<BoardCommentPagingResponse> showBoardComments(@CurrentMember Member member,
                                                                      @PathVariable(value = "boardId") UUID boardId,
                                                                      @PageableDefault(page =1, sort = "created_at")
                                                                          Pageable pageable) {
        return BaseResponse.onSuccess(boardCommentService.showBoardComments(member, boardId,pageable));
    }



}
