package com.umc.networkingService.domain.album.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlbumHeartResponse {
    private boolean isChecked;
    private int heartCnt;
}
