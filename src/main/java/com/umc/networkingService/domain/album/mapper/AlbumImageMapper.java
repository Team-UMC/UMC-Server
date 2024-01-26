package com.umc.networkingService.domain.album.mapper;

import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumImage;
import org.springframework.stereotype.Component;

@Component
public class AlbumImageMapper {
    public AlbumImage toAlbumImage(Album album, String url){
        return AlbumImage.builder()
                .album(album)
                .url(url)
                .build();
    }
}
