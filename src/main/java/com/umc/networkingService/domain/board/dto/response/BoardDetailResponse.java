package com.umc.networkingService.domain.board.dto.response;

import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardDetailResponse {
    private HostType hostType;
    private BoardType boardType;
    private String writer;
    private String profileImage;
    private Part part;
    private Semester semester;
    private String title;
    private String content;
    private List<String> boardFiles;
    private int hitCount;
    private int heartCount;
    private int commentCount;
    private boolean isLiked;
    private LocalDateTime createdAt;
}
