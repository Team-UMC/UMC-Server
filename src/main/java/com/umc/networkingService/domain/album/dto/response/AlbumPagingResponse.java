package com.umc.networkingService.domain.album.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumPagingResponse<T> {
    private List<T> albumPageResponses = new ArrayList<>();
    private int page;
    private int totalPages;
    private Boolean isFirst;
    private Boolean isLast;
    private int totalElements;
}
