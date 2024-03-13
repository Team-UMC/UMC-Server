package com.umc.networkingService.domain.album.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.album.dto.request.AlbumCommentCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumCommentUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumCommentResponse;
import com.umc.networkingService.domain.album.service.AlbumCommentService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "사진첩 댓글 API", description = "사진첩 댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/albums/comments")
public class AlbumCommentController {

    private final AlbumCommentService albumCommentService;

    @Operation(summary = "댓글 작성 API", description = "댓글을 작성하는 API입니다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "ALBUM001", description = "존재하지 않는 사진첩입니다.")
    })
    @PostMapping
    public BaseResponse<AlbumCommentResponse> createAlbumComment(
            @CurrentMember Member member,
            @Valid @RequestBody AlbumCommentCreateRequest request) {
        return BaseResponse.onSuccess(albumCommentService.createAlbumComment(member, request));
    }

    @Operation(summary = "댓글 수정 API", description = "댓글을 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMENT001", description = "댓글을 찾을 수 없을 경우 발생"),
            @ApiResponse(responseCode = "COMMENT002", description = "댓글 수정 권한이 없을 경우 발생")
    })
    @PatchMapping("/{commentId}")
    public BaseResponse<AlbumCommentResponse> updateAlbumComment(
            @CurrentMember Member member,
            @PathVariable(value = "commentId") UUID commentId,
            @Valid @RequestBody AlbumCommentUpdateRequest request) {
        return BaseResponse.onSuccess(albumCommentService.updateAlbumComment(member, commentId, request));
    }

    @Operation(summary = "댓글 삭제 API", description = "댓글을 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMENT001", description = "댓글을 찾을 수 없을 경우 발생"),
            @ApiResponse(responseCode = "COMMENT002", description = "댓글 수정 권한이 없을 경우 발생")
    })
    @DeleteMapping("/{commentId}")
    public BaseResponse<AlbumCommentResponse> deleteAlbumComment(@CurrentMember Member member,
                                                                 @PathVariable(value = "commentId") UUID commentId) {
        return BaseResponse.onSuccess(albumCommentService.deleteAlbumComment(member, commentId));
    }
}
