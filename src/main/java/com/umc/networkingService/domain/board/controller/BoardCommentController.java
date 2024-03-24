package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.board.dto.request.BoardCommentRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.BoardCommentPageInfos;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.service.BoardCommentService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.BoardCommentId;
import static com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.BoardCommentPageElement;
import static com.umc.networkingService.domain.board.dto.response.BoardResponse.*;

@Tag(name = "게시판 댓글 API", description = "게시판 댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/comments")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @Operation(summary = "댓글 작성 API", description = "댓글을 작성하는 API입니다. + 대댓글일 경우, 부모 댓글의 commentId를 param으로 주세요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생"),
            @ApiResponse(responseCode = "COMMENT001", description = "댓글을 찾을 수 없을 경우 발생")
    })
    @PostMapping
    public BaseResponse<BoardCommentId> addBoardComment(@CurrentMember Member member,
                                                        @RequestParam(required = false) UUID commentId,
                                                        @Valid @RequestBody BoardCommentRequest.BoardCommentAddRequest request) {
        return BaseResponse.onSuccess(boardCommentService.addBoardComment(member, commentId, request));
    }

    @Operation(summary = "댓글 수정 API", description = "댓글을 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMENT001", description = "댓글을 찾을 수 없을 경우 발생"),
            @ApiResponse(responseCode = "COMMENT002", description = "댓글 수정 권한이 없을 경우 발생"),
            
    })
    @PatchMapping("/{commentId}")
    public BaseResponse<BoardCommentId> updateBoardComment(@CurrentMember Member member,
                                                           @PathVariable(value = "commentId") UUID commentId,
                                                           @Valid @RequestBody BoardCommentRequest.BoardCommentUpdateRequest request) {
        return BaseResponse.onSuccess(boardCommentService.updateBoardComment(member, commentId, request));
    }


    @Operation(summary = "댓글 삭제 API", description = "댓글을 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMENT001", description = "댓글을 찾을 수 없을 경우 발생"),
            @ApiResponse(responseCode = "COMMENT002", description = "댓글 삭제 권한이 없을 경우 발생")
    })
    @DeleteMapping("/{commentId}")
    public BaseResponse<BoardCommentId> deleteBoardComment(@CurrentMember Member member,
                                                           @PathVariable(value = "commentId") UUID commentId) {
        return BaseResponse.onSuccess(boardCommentService.deleteBoardComment(member, commentId));
    }

    @Operation(summary = " 특정 게시글 댓글 목록 조회 API", description = "특정 게시글의 댓글 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생")
    })
    @Parameters(value = {
            @Parameter(name = "page", description = " page 시작은 0번부터, 오름차순으로 조회됩니다.")
    })
    @GetMapping(value = "/{boardId}")
    public BaseResponse<BoardCommentPageInfos<BoardCommentPageElement>> showBoardComments(@CurrentMember Member member,
                                                                                          @PathVariable(value = "boardId") UUID boardId,
                                                                                          @PageableDefault(sort = "created_at", direction = Sort.Direction.ASC)
                                                                      @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(boardCommentService.showBoardComments(member, boardId, pageable));
    }

    @Operation(summary = "[APP] 내가 댓글 쓴 글 조회/검색 API", description = "APP용 내가 댓글 쓴 글을 조회/검색하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @Parameters(value = {
            @Parameter(name = "keyword", description = "keyword를 주지 않으면 모든 내가 댓글 쓴 글이 조회됩니다. keyword를 주면 검색이 가능합니다."),
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다.")
    })
    @GetMapping(value = "/member/comments/app")
    public BaseResponse<BoardPageInfos<MyBoardPageElement>> showBoardsByMemberCommentsForApp(@CurrentMember Member member,
                                                                                             @RequestParam(name = "keyword", required = false) String keyword,
                                                                                             @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC)
                                                                                @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(boardCommentService.showBoardsByMemberCommentForApp(member, keyword, pageable));
    }

    @Operation(summary = "[WEB] 내가 댓글 쓴 글 조회/검색 API", description = "WEB용 내가 댓글 쓴 글을 조회/검색하는 API입니다.  "+
            "host: CENTER, BRANCH, CAMPUS 중 하나의 값을 대문자로 주세요.  " +
            "board: NOTICE, FREE, WORKBOOK, OB, QUESTION 중 하나의 값을 대문자로 주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMON405", description = "host, board type이 적절하지 않은 값일 경우 발생")
    })
    @Parameters(value = {
            @Parameter(name = "keyword", description = "keyword를 주지 않으면 모든 내가 댓글 쓴 글이 조회됩니다. keyword를 주면 검색이 가능합니다."),
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다.")})
    @GetMapping(value = "/member/comments/web")
    public BaseResponse<BoardCommentPageInfos<MyBoardCommentPageElement>> showBoardsByMemberCommentForWeb(@CurrentMember Member member,
                                                                                 @RequestParam(name = "host") HostType hostType,
                                                                                 @RequestParam(name = "board") BoardType boardType,
                                                                                 @RequestParam(name = "keyword", required = false) String keyword,
                                                                                 @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC)
                                                                                          @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(boardCommentService.showBoardsByMemberCommentForWeb(member, hostType, boardType,keyword, pageable));
    }


}
