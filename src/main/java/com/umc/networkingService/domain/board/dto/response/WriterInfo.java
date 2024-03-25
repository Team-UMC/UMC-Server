package com.umc.networkingService.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriterInfo {
    private String writer;
    private String profileImage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Part part;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Semester semester;
}