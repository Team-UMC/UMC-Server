package com.umc.networkingService.domain.album.dto.response;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AlbumInquiryFeaturedResponse {
    private UUID albumId;
    private String title;
    private String thumbnail;
    private String createdAt;
}
