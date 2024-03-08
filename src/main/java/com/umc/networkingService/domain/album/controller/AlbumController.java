package com.umc.networkingService.domain.album.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.*;
import com.umc.networkingService.domain.album.service.AlbumService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.BaseResponse;
import com.umc.networkingService.global.common.enums.Semester;
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

@Tag(name = "사진첩 API", description = "사진첩 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "사진첩 작성 API", description = "사진첩을 작성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "IMAGE001", description = "파일 S3 업로드 실패한 경우")
    })
    @PostMapping(value = "/staff/albums", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<AlbumIdResponse> createAlbum(
            @CurrentMember Member member,
            @Valid @RequestPart("request") AlbumCreateRequest request,
            @RequestPart(name = "albumImages", required = false) List<MultipartFile> albumImages) {
        return BaseResponse.onSuccess(albumService.createAlbum(member, request, albumImages));
    }

    @Operation(summary = "사진첩 수정 API", description = "사진첩을 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "ALBUM001", description = "존재하지 않는 사진첩입니다."),
            @ApiResponse(responseCode = "ALBUM002", description = "해당 사진첩에 대한 권한이 없습니다."),
            @ApiResponse(responseCode = "IMAGE001", description = "파일 S3 업로드 실패한 경우")
    })
    @PatchMapping(value = "/staff/albums/{albumId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<AlbumIdResponse> updateAlbum(
            @CurrentMember Member member,
            @PathVariable(value = "albumId") UUID albumId,
            @Valid @RequestPart("request") AlbumUpdateRequest request,
            @RequestPart(name = "albumImages", required = false) List<MultipartFile> albumImages) {
        return BaseResponse.onSuccess(albumService.updateAlbum(member, albumId, request, albumImages));
    }

    @Operation(summary = "사진첩 삭제 API", description = "사진첩을 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "ALBUM001", description = "존재하지 않는 사진첩입니다."),
            @ApiResponse(responseCode = "ALBUM002", description = "해당 사진첩에 대한 권한이 없습니다.")
    })
    @DeleteMapping("/staff/albums/{albumId}")
    public BaseResponse<AlbumIdResponse> deleteAlbum(
            @CurrentMember Member member,
            @PathVariable(value = "albumId") UUID albumId) {
        return BaseResponse.onSuccess(albumService.deleteAlbum(member, albumId));
    }

    @Operation(summary = "사진첩 조회 API", description = "사진첩을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @Parameters(value = {
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다."),
            @Parameter(name = "semester", description = "기수 조건이 있을 경우 입력하는 파라미터입니다."),
    })
    @GetMapping("/albums")
    public BaseResponse<AlbumPagingResponse<AlbumInquiryResponse>> inquiryAlbums(
            @CurrentMember Member member,
            @RequestParam(required = false) Semester semester,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(albumService.inquiryAlbums(member, semester, pageable));
    }

    @Operation(summary = "대표 사진첩 조회 API", description = "대표 사진첩을 조회하는 API입니다.(좋아요 순)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
    })
    @Parameters(value = {
            @Parameter(name = "page", description = "page 시작은 0번부터, 내림차순으로 조회됩니다.(한 페이지당 10개)"),
    })
    @GetMapping("/albums/featured")
    public BaseResponse<AlbumPagingResponse<AlbumInquiryFeaturedResponse>> inquiryAlbumsFeatured(
            @CurrentMember Member member,
            @PageableDefault(sort = "heartCount", direction = Sort.Direction.DESC, size = 10)
            @Parameter(hidden = true) Pageable pageable) {
        return BaseResponse.onSuccess(albumService.inquiryAlbumsFeatured(member, pageable));
    }

    @Operation(summary = "사진첩 상세 조회 API", description = "특정 사진첩을 상세 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "ALBUM001", description = "존재하지 않는 사진첩입니다.")
    })
    @GetMapping("/albums/{albumId}")
    public BaseResponse<AlbumDetailResponse> inquiryAlbumDetail(
            @CurrentMember Member member,
            @PathVariable(value = "albumId") UUID albumId) {
        return BaseResponse.onSuccess(albumService.inquiryAlbumDetail(member, albumId));
    }

    @Operation(summary = "사진첩 좋아요/취소 API", description = "클릭 시 좋아요 or 취소 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "ALBUM001", description = "존재하지 않는 사진첩입니다.")
    })
    @PostMapping("/{albumId}/heart")
    public BaseResponse<AlbumIdResponse> toggleAlbumLike(@CurrentMember Member member,
                                                         @PathVariable(value = "albumId") UUID albumId) {
        return BaseResponse.onSuccess(albumService.toggleAlbumLike(member, albumId));
    }
}
