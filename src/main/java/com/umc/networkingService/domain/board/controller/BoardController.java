package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.board.dto.request.BoardRequest;
import com.umc.networkingService.domain.board.dto.response.BoardResponse;
import com.umc.networkingService.domain.board.dto.response.MyBoardResponse;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.service.BoardService;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "게시판 API", description = "게시판 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;


    @Operation(summary = "게시글 작성 API", description = "게시글을 작성하는 API입니다.  " +
            " host : CENTER, BRANCH, CAMPUS 중 하나의 값을 대문자로 주세요.  " +
            " board : NOTICE, FREE, WORKBOOK, OB, QUESTION 중 하나의 값을 대문자로 주세요.  " +
            " NOTICE는 해당 hostType 이하 운영진만 작성 가능합니다. WORKBOOK 게시판은 host : CAMPUS에서 교내 운영진만 작성 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMON402", description = "request 요소들의 validation 검증에 실패할 경우 발생"),
            @ApiResponse(responseCode = "BOARD001", description = "WORKBOOK 게시판과 CENTER, BRANCH를 선택했을 경우 금지된 요청"),
            @ApiResponse(responseCode = "BOARD003", description = "게시글을 작성할 권한이 없을 경우 발생"),
            @ApiResponse(responseCode = "FILE001", description = "파일 S3 업로드 실패할 경우 발생")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<BoardResponse.BoardId> createBoard(@CurrentMember Member member,
                                                           @Valid @RequestPart("request") BoardRequest.BoardCreateRequest request,
                                                           @RequestPart(name = "file", required = false) List<MultipartFile> files) {
        return BaseResponse.onSuccess(boardService.createBoard(member, request, files));
    }


    @Operation(summary = "게시글 수정 API", description = "게시글을 수정하는 API입니다.  " +
            "host : CENTER, BRANCH, CAMPUS 중 하나의 값을 대문자로 주세요.  " +
            "board : NOTICE, FREE, WORKBOOK, OB, QUESTION 중 하나의 값을 대문자로 주세요.  " +
            "NOTICE는 해당 hostType 이하 운영진만 수정 가능합니다. WORKBOOK 게시판은 host : CAMPUS에서 교내 운영진만 수정 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMON402", description = "request 요소들의 validation 검증에 실패할 경우 발생"),
            @ApiResponse(responseCode = "BOARD001", description = "WORKBOOK 게시판과 CENTER, BRANCH를 선택했을 경우 금지된 요청"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생"),
            @ApiResponse(responseCode = "BOARD003", description = "게시글을 수정할 권한이 없을 경우 발생"),
            @ApiResponse(responseCode = "FILE001", description = "파일 S3 업로드 실패할 경우 발생")
    })
    @PatchMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<BoardResponse.BoardId> updateBoard(@CurrentMember Member member,
                                                           @PathVariable(value = "boardId") UUID boardId,
                                                           @Valid @RequestPart("request") BoardRequest.BoardUpdateRequest request,
                                                           @RequestPart(name = "file", required = false) List<MultipartFile> files) {

        return BaseResponse.onSuccess(boardService.updateBoard(member, boardId, request, files));
    }

    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제하는 API입니다. 운영진은 멤버 게시글 삭제 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생"),
            @ApiResponse(responseCode = "BOARD003", description = "게시글을 삭제할 권한이 없을 경우 발생"),
    })
    @DeleteMapping("/{boardId}")
    public BaseResponse<BoardResponse.BoardId> deleteBoard(@CurrentMember Member member,
                                                           @PathVariable(value = "boardId") UUID boardId) {
        return BaseResponse.onSuccess(boardService.deleteBoard(member, boardId));
    }

    @Operation(summary = "핀고정된 notice 조회 API", description = "핀 고정된 공지 게시글을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @GetMapping("/pinned")
    public BaseResponse<BoardResponse.PinnedNotices> showPinnedNotices(@CurrentMember Member member) {
        return BaseResponse.onSuccess(boardService.showPinnedNotices(member));
    }

    @Operation(summary = "특정 게시판의 게시글 목록 조회 API", description = "특정 게시판의 게시글 목록을 조회하는 API입니다.  " +
            "host: CENTER, BRANCH, CAMPUS 중 하나의 값을 대문자로 주세요.  " +
            "board: NOTICE, FREE, WORKBOOK, OB, QUESTION 중 하나의 값을 대문자로 주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMON405", description = "host, board type이 적절하지 않은 값일 경우 발생")
    })
    @Parameters(value = {
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다."),
    })
    @GetMapping
    public BaseResponse<BoardResponse.BoardPageInfos> showBoards(@CurrentMember Member member,
                                                                 @RequestParam(name = "host") HostType hostType,
                                                                 @RequestParam(name = "board") BoardType boardType,
                                                                 @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC)
                                                        @Parameter(hidden = true) Pageable pageable) {

        return BaseResponse.onSuccess(boardService.showBoards(member, hostType, boardType, pageable));
    }

    @Operation(summary = "특정 게시글 상세 조회 API", description = "단일 게시글을 boardId를 통해 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생"),
            @ApiResponse(responseCode = "BOARD003", description = "게시글을 조회할 권한이 없을 경우 발생"),
    })
    @GetMapping("/{boardId}")
    public BaseResponse<BoardResponse.BoardDetail> showBoardDetail(@CurrentMember Member member,
                                                                   @PathVariable(value = "boardId") UUID boardId) {
        return BaseResponse.onSuccess(boardService.showBoardDetail(member, boardId));
    }


    @Operation(summary = "게시글 검색 API", description = "keyword로 게시글을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @Parameters(value = {
            @Parameter(name = "keyword", description = "검색할 keyword를 입력해주세요."),
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다."),
    })
    @GetMapping(value = "/search")
    public BaseResponse<BoardResponse.BoardSearchPageInfos> searchBoard(@CurrentMember Member member,
                                                                        @RequestParam(name = "keyword") String keyword,
                                                                        @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC)
                                                               @Parameter(hidden = true) Pageable pageable) {

        return BaseResponse.onSuccess(boardService.searchBoard(member, keyword, pageable));
    }

    @Operation(summary = "[APP] 내가 쓴 게시글 조회/검색 API", description = "APP용 내가 쓴 게시글을 조회하거나 검색하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @Parameters(value = {
            @Parameter(name = "keyword", description = "keyword를 주지 않으면 모든 내가 쓴 게시글이 조회됩니다. keyword를 주면 검색이 가능합니다."),
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다."),
    })
    @GetMapping(value = "/member/app")
    public BaseResponse<MyBoardResponse.MyBoardPageInfos> showBoardsByMemberForApp(@CurrentMember Member member,
                                                                  @RequestParam(name = "keyword", required = false) String keyword,
                                                                  @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC)
                                                                  @Parameter(hidden = true) Pageable pageable) {

        return BaseResponse.onSuccess(boardService.showBoardsByMemberForApp(member, keyword, pageable));
    }

    @Operation(summary = "[WEB] 내가 쓴 게시글 조회/검색 API", description = "WEB용 내가 쓴 게시글을 조회하거나 검색하는 API입니다.  "+
            "host: CENTER, BRANCH, CAMPUS 중 하나의 값을 대문자로 주세요.  " +
            "board: NOTICE, FREE, WORKBOOK, OB, QUESTION 중 하나의 값을 대문자로 주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMON405", description = "host, board type이 적절하지 않은 값일 경우 발생")
    })
    @Parameters(value = {
            @Parameter(name = "keyword", description = "keyword를 주지 않으면 모든 내가 쓴 게시글이 조회됩니다. keyword를 주면 검색이 가능합니다."),
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다."),
    })
    @GetMapping(value = "/member/web")
    public BaseResponse<MyBoardResponse.MyBoardPageInfos> showBoardsByMemberForWeb(@CurrentMember Member member,
                                                                        @RequestParam(name = "host") HostType hostType,
                                                                        @RequestParam(name = "board") BoardType boardType,
                                                                        @RequestParam(name = "keyword", required = false) String keyword,
                                                                        @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC)
                                                                        @Parameter(hidden = true) Pageable pageable) {

        return BaseResponse.onSuccess(boardService.showBoardsByMemberForWeb(member, hostType,boardType, keyword, pageable));
    }


    @Operation(summary = "[APP] 내가 좋아요한 게시글 조회/검색 API", description = "APP용 내가 좋아요한 게시글을 조회하거나 검색하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @Parameters(value = {
            @Parameter(name = "keyword", description = "keyword를 주지 않으면 내가 좋아요한 모든 글이 조회됩니다. keyword를 주면 검색이 가능합니다."),
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다."),
    })
    @GetMapping(value = "/member/hearts/app")
    public BaseResponse<MyBoardResponse.MyBoardPageInfos> showMemberBoardHeartForApp(@CurrentMember Member member,
                                                                     @RequestParam(name = "keyword", required = false) String keyword,
                                                                     @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC)
                                                                     @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(boardService.showBoardsByMemberHeartForApp(member, keyword, pageable));
    }

    @Operation(summary = "[WEB] 내가 좋아요한 게시글 조회/검색 API", description = "WEB용 내가 좋아요한 게시글을 조회하거나 검색하는 API입니다.  "+
            "host: CENTER, BRANCH, CAMPUS 중 하나의 값을 대문자로 주세요.  " +
            "board: NOTICE, FREE, WORKBOOK, OB, QUESTION 중 하나의 값을 대문자로 주세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMON405", description = "host, board type이 적절하지 않은 값일 경우 발생")
    })
    @Parameters(value = {
            @Parameter(name = "keyword", description = "keyword를 주지 않으면 내가 좋아요한 모든 글이 조회됩니다. keyword를 주면 검색이 가능합니다."),
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다."),
    })
    @GetMapping(value = "/member/hearts/web")
    public BaseResponse<MyBoardResponse.MyBoardPageInfos> showMemberBoardHeartForWeb(@CurrentMember Member member,
                                                                    @RequestParam(name = "host") HostType hostType,
                                                                    @RequestParam(name = "board") BoardType boardType,
                                                                    @RequestParam(name = "keyword", required = false) String keyword,
                                                                    @PageableDefault(sort = "created_at", direction = Sort.Direction.DESC)
                                                                          @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(boardService.showBoardsByMemberHeartForWeb(member, hostType, boardType, keyword, pageable));
    }


    @Operation(summary = "게시글 좋아요/취소 API", description = "한번 클릭하면 좋아요, 한번 더 클릭하면 취소됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생")
    })
    @PostMapping("/{boardId}/heart")
    public BaseResponse<BoardResponse.BoardId> toggleBoardLike(@CurrentMember Member member,
                                                               @PathVariable(value = "boardId") UUID boardId) {
        return BaseResponse.onSuccess(boardService.toggleBoardLike(member, boardId));
    }

}
