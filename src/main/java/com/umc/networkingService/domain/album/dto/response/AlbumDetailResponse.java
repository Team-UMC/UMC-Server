package com.umc.networkingService.domain.album.dto.response;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AlbumDetailResponse {
    private String writer;
    private String profileImage;
    private Semester semester;
    private String title;
    private String content;
    private List<String> albumImages;
    private int hitCount;
    private int heartCount;
    private int commentCount;
    private boolean isLiked;
    private LocalDateTime createdAt;
}
