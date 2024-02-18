package com.umc.networkingService.domain.album.dto.response;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AlbumPageResponse {
    private UUID albumId;
    private String writer;
    private String profileImage;
    private Semester semester;
    private String title;
    private String content;
    private String thumbnail;
    private int hitCount;
    private int heartCount;
    private int commentCount;
    private LocalDateTime createdAt;
}
