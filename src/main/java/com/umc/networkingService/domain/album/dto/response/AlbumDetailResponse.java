package com.umc.networkingService.domain.album.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

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
    private Boolean isLiked;
    private Boolean isMine;
    private String createdAt;
}
