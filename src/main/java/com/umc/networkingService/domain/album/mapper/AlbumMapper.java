package com.umc.networkingService.domain.album.mapper;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper {

    public Album createAlbum(Member member, AlbumCreateRequest request) {
        return Album.builder()
                .writer(member)
                .semester(request.getSemester())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
