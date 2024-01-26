package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumIdResponse;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.mapper.AlbumMapper;
import com.umc.networkingService.domain.album.repository.AlbumRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService{

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final AlbumImageService albumImageService;

    @Override
    public AlbumIdResponse createAlbum(Member member, AlbumCreateRequest request, List<MultipartFile> albumImages) {

        Album album = albumRepository.save(albumMapper.createAlbum(member, request));

        if(albumImages != null)
            albumImageService.uploadAlbumImages(album, albumImages);

        return new AlbumIdResponse(album.getId());
    }

    @Override
    public AlbumIdResponse updateAlbum(Member member, UUID albumId, AlbumUpdateRequest request, List<MultipartFile> albumImages) {
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new RestApiException(
                ErrorCode.EMPTY_ALBUM));

        if (!album.getWriter().getId().equals(member.getId())) {
            throw new RestApiException(ErrorCode.NO_AUTHORIZATION_ALBUM);
        }

        album.updateAlbum(request.getTitle(), request.getTitle(), request.getSemester());

        albumImageService.updateAlbumImages(album, albumImages);

        return new AlbumIdResponse(album.getId());
    }
}
