package com.umc.networkingService.domain.album.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumIdResponse;
import com.umc.networkingService.domain.album.service.AlbumService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Tag(name = "사진첩 API", description = "사진첩 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "사진첩 작성 API", description = "사진첩을 작성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "IMAGE001", description = "파일 S3 업로드 실패한 경우")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<AlbumIdResponse> createAlbum(@CurrentMember Member member,
                                                     @Valid @RequestPart("request")AlbumCreateRequest request,
                                                     @RequestPart(name = "albumImage", required = false) List<MultipartFile> albumImages) {
        return BaseResponse.onSuccess(albumService.createAlbum(member, request, albumImages));
    }

    @Operation(summary = "사진첩 수정 API", description = "사진첩을 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "ALBUM001", description = "존재하지 않는 사진첩입니다."),
            @ApiResponse(responseCode = "ALBUM002", description = "해당 사진첩에 대한 권한이 없습니다."),
            @ApiResponse(responseCode = "IMAGE001", description = "파일 S3 업로드 실패한 경우")
    })
    @PatchMapping(value = "/{albumId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<AlbumIdResponse> updateAlbum(@CurrentMember Member member,
                                                     @PathVariable(value = "albumId") UUID albumId,
                                                     @Valid @RequestPart("request")AlbumUpdateRequest request,
                                                     @RequestPart(name = "albumImage", required = false) List<MultipartFile> albumImages) {
        return BaseResponse.onSuccess(albumService.updateAlbum(member, albumId, request, albumImages));
    }

    @Operation(summary = "사진첩 삭제 API", description = "사진첩을 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "ALBUM001", description = "존재하지 않는 사진첩입니다."),
            @ApiResponse(responseCode = "ALBUM002", description = "해당 사진첩에 대한 권한이 없습니다.")
    })
    @DeleteMapping("/{albumId}")
    public BaseResponse<AlbumIdResponse> deleteAlbum(@CurrentMember Member member,
                                                     @PathVariable(value = "albumId") UUID albumId) {
        return BaseResponse.onSuccess(albumService.deleteAlbum(member, albumId));
    }
}
