package com.umc.networkingService.domain.album.mapper;

import com.umc.networkingService.domain.album.dto.request.AlbumCommentCreateRequest;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumComment;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class AlbumCommentMapper {
    public AlbumComment createAlbumComment(Member member, AlbumComment parent, Album album, String content) {
        return AlbumComment.builder()
                .writer(member)
                .parentComment(parent)
                .content(content)
                .album(album)
                .build();
    }
}
