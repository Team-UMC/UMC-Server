package com.umc.networkingService.domain.album.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlbumHeartResponse {
    private Boolean isChecked;
    private int heartCnt;
}
