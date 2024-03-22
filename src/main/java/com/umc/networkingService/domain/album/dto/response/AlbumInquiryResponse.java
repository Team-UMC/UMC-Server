package com.umc.networkingService.domain.album.dto.response;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AlbumInquiryResponse {
    private UUID albumId;
    private WriterInfo writer;
    private Semester semester;
    private String title;
    private String thumbnail;
    private int imageCnt;
    private int hitCount;
    private int heartCount;
    private int commentCount;
    private String createdAt;
}
