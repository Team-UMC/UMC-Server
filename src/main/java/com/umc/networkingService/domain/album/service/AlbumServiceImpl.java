package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumIdResponse;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.mapper.AlbumMapper;
import com.umc.networkingService.domain.album.repository.AlbumRepository;
import com.umc.networkingService.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService{

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final AlbumImageService albumImageService;

    @Override
    public AlbumIdResponse createAlbum(Member member, AlbumCreateRequest request, List<MultipartFile> albumImage) {

        Album album = albumRepository.save(albumMapper.createAlbum(member, request));

        if(albumImage != null)
            albumImageService.uploadAlbumImages(album, albumImage);

        return new AlbumIdResponse(album.getId());
    }
}
