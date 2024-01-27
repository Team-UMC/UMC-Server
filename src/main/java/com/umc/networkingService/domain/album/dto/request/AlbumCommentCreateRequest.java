package com.umc.networkingService.domain.album.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumCommentCreateRequest {

    @NotNull(message = "albumId는 필수 입력값입니다.")
    private UUID albumId;

    @NotBlank(message = "content는 필수 입력값입니다.")
    private String content;
}
