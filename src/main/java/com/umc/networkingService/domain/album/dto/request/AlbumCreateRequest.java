package com.umc.networkingService.domain.album.dto.request;

import com.umc.networkingService.global.common.enums.Semester;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumCreateRequest {

    @NotBlank(message = "title은 필수 입력값입니다.")
    private String title;

    @NotBlank(message = "content는 필수 입력갑입니다.")
    private String content;

    private Semester semester;
}
