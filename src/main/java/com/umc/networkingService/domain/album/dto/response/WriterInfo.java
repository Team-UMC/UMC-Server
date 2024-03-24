package com.umc.networkingService.domain.album.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriterInfo {
    private String writer;
    private String profileImage;
    private String position;
}
