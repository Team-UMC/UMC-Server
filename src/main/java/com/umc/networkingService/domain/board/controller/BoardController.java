package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCreateResponse;
import com.umc.networkingService.domain.board.service.BoardService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @PostMapping("/")
    public BaseResponse<BoardCreateResponse> boardCreate(@CurrentMember Member member,
                                                         @Valid @RequestPart BoardCreateRequest request, @RequestPart(value = "file", required = false)
                                                         List<MultipartFile> files) {
        return BaseResponse.onSuccess(boardService.boardCreate(member, request, files));
    }


}
