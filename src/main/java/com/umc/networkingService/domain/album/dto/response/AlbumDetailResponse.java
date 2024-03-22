package com.umc.networkingService.domain.album.dto.response;

import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AlbumDetailResponse {
    private WriterInfo writer;
    private String title;
    private String content;
    private List<String> albumImages;
    private int hitCount;
    private int heartCount;
    private int commentCount;
    private boolean isLiked;
    private boolean isMine;
    private String createdAt;
}
