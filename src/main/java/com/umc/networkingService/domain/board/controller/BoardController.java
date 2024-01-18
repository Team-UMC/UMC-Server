package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCreateResponse;
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

@Tag(name = "Board API", description = "게시판 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @Operation(summary = "게시글 작성 API", description = "게시글을 작성하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200",description = "성공"),
            @ApiResponse(responseCode = "BOARD001",description = "에러 어떤거??"),
    })
    @PostMapping("/")
    public BaseResponse<BoardCreateResponse> boardCreate(@CurrentMember Member member,
                                                         @Valid @RequestPart BoardCreateRequest request, @RequestPart(value = "file", required = false)
                                                             List<MultipartFile> files) {
        return BaseResponse.onSuccess(boardService.boardCreate(member, request, files));
    }


}
