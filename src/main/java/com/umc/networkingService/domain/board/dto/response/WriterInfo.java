package com.umc.networkingService.domain.board.dto.response;

import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriterInfo {
    private String writer;
    private String profileImage;
    private Part part;
    private Semester semester;
}