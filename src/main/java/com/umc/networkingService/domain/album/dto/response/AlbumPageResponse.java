package com.umc.networkingService.domain.album.dto.response;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public class AlbumPageResponse {
    private UUID albumId;
    private String writer;
    private String profileImage;
    private Semester semester;
    private String title;
    private String content;
    private int hitCount;
    private int heartCount;
    private int commentCount;
    private LocalDateTime createdAt;
}
