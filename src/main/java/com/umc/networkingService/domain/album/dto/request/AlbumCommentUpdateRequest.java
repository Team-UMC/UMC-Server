package com.umc.networkingService.domain.album.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumCommentUpdateRequest {
    @NotBlank(message = "content는 필수 입력갑입니다.")
    private String content;
}
