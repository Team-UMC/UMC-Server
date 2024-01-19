package com.umc.networkingService.domain.board.dto.request;

import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.global.common.validation.annotation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateRequest {
    @NotNull(message = "CENTER, BRANCH, CAMPUS 중 하나를 선택해주세요.")
    @ValidEnum(message = "옳지 않은 값입니다. CENTER, BRANCH, CAMPUS 중 하나를 선택해주세요.", enumClass = HostType.class)
    private HostType hostType;

    @NotNull(message = "게시판 구분을 선택해주세요.")
    @ValidEnum(message = "옳지 않은 값입니다. NOTICE, FREE ,WORKBOOK, QUESTION, STAFF, OB 중 하나를 선택해주세요.",
            enumClass = BoardType.class)
    private BoardType boardType;

    @NotBlank(message = "title은 필수 입력값입니다.")
    private String title;

    @NotBlank(message = "content는 필수 입력값입니다.")
    private String content;
}
