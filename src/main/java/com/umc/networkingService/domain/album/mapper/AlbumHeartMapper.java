package com.umc.networkingService.domain.album.mapper;

import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumHeart;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class AlbumHeartMapper {

    public AlbumHeart toAlbumHeartEntity(Album album, Member member) {
        return AlbumHeart.builder()
                .album(album)
                .member(member)
                .isChecked(false)
                .build();
    }
}
