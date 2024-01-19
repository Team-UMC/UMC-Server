package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.service.BoardService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}
