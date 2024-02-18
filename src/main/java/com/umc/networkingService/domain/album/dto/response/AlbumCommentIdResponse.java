package com.umc.networkingService.domain.album.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumCommentIdResponse {
    private UUID commentId;
}
