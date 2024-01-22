package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardDetailResponse;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.service.BoardService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Board API", description = "게시판 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;


    @Operation(summary = "게시글 작성 API", description = "게시글을 작성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "MEMBER002", description = "게시글을 쓸 권한이 없을 경우 발생"),
            @ApiResponse(responseCode = "BOARD001", description = "WORKBOOK 게시판과 CENTER, BRANCH를 선택했을 경우 금지된 요청")
    })
    @PostMapping
    public BaseResponse<BoardIdResponse> createBoard(@CurrentMember Member member,
                                                     @Valid @RequestPart("request") BoardCreateRequest request,
                                                     @RequestPart(name = "file", required = false) List<MultipartFile> files) {
        return BaseResponse.onSuccess(boardService.createBoard(member, request, files));
    }


    @Operation(summary = "게시글 수정 API", description = "게시글을 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "MEMBER002", description = "게시글을 쓸 권한이 없을 경우 발생"),
            @ApiResponse(responseCode = "BOARD001", description = "WORKBOOK 게시판과 CENTER, BRANCH를 선택했을 경우 금지된 요청"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생")
    })
    @PatchMapping("/{boardId}")
    public BaseResponse<BoardIdResponse> updateBoard(@CurrentMember Member member,
                                                         @PathVariable(value="boardId") UUID boardId,
                                                         @Valid @RequestPart("request") BoardUpdateRequest request,
                                                     @RequestPart(name = "file", required = false) List<MultipartFile> files) {
        return BaseResponse.onSuccess(boardService.updateBoard(member,boardId, request, files));
    }

    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생")
    })
    @DeleteMapping("/{boardId}")
    public BaseResponse<BoardIdResponse> deleteBoard(@CurrentMember Member member,
                                                     @PathVariable(value="boardId") UUID boardId) {
        return BaseResponse.onSuccess(boardService.deleteBoard(member,boardId));
    }


    @Operation(summary = "특정 게시판의 게시글 목록 조회 API", description = "특정 게시판의 게시글 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),

    })
    @GetMapping
    public BaseResponse<BoardPagingResponse> showBoards(@CurrentMember Member member,
                                                        @RequestParam(name = "host") HostType hostType,
                                                        @RequestParam(name = "board") BoardType boardType,
                                                        @PageableDefault(page = 1, sort = "created_at",
                                                                direction = Sort.Direction.DESC) Pageable pageable) {

        return BaseResponse.onSuccess(boardService.showBoards(member,hostType,boardType,pageable));
    }

    @Operation(summary = "특정 게시글 상세 조회 API", description = "단일 게시글을 boardId를 통해 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "BOARD002", description = "게시글을 찾을 수 없을 경우 발생")

    })
    @GetMapping("/{boardId}")
    public BaseResponse<BoardDetailResponse> showBoardDetail(@CurrentMember Member member,
                                                             @PathVariable(value="boardId") UUID boardId) {
        return BaseResponse.onSuccess(boardService.showBoardDetail(member, boardId));
    }


    @Operation(summary = "게시글 검색 API", description = "keyword로 게시글을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),

    })
    @GetMapping("/search")
    public BaseResponse<BoardPagingResponse> searchBoard(@CurrentMember Member member,
                                                         @RequestParam(name = "keyword") String keyword,
                                                         @PageableDefault(page = 1, sort = "created_at",
                                                                 direction = Sort.Direction.DESC) Pageable pageable) {

        return BaseResponse.onSuccess(boardService.searchBoard(member, keyword, pageable));
    }

    @Operation(summary = "게시글 추천/취소 API", description = "한번 클릭하면 추천, 한번 더 클릭하면 취소됩니다.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @PostMapping ("/{boardId}/heart")
    public BaseResponse<BoardIdResponse> toggleBoardLike(@CurrentMember Member member,
                                                   @PathVariable(value = "boardId") UUID boardId) {
        return BaseResponse.onSuccess(boardService.toggleBoardLike(member,boardId));
    }

}
