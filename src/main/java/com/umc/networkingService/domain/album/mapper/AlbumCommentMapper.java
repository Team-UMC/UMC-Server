package com.umc.networkingService.domain.album.mapper;

import com.umc.networkingService.domain.album.dto.request.AlbumCommentCreateRequest;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumComment;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class AlbumCommentMapper {
    public AlbumComment createAlbumComment(Member member, Album album, AlbumCommentCreateRequest request) {
        return AlbumComment.builder()
                .writer(member)
                .content(request.getContent())
                .album(album)
                .build();
    }
}