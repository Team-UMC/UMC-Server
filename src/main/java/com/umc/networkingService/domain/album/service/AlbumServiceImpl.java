package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumDetailResponse;
import com.umc.networkingService.domain.album.dto.response.AlbumIdResponse;
import com.umc.networkingService.domain.album.dto.response.AlbumPagingResponse;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumHeart;
import com.umc.networkingService.domain.album.entity.AlbumImage;
import com.umc.networkingService.domain.album.mapper.AlbumHeartMapper;
import com.umc.networkingService.domain.album.mapper.AlbumMapper;
import com.umc.networkingService.domain.album.repository.AlbumHeartRepository;
import com.umc.networkingService.domain.album.repository.AlbumRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService{

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final AlbumImageService albumImageService;
    private final AlbumHeartRepository albumHeartRepository;
    private final AlbumHeartMapper albumHeartMapper;

    @Override
    @Transactional
    public AlbumIdResponse createAlbum(Member member, AlbumCreateRequest request, List<MultipartFile> albumImages) {

        Album album = albumRepository.save(albumMapper.createAlbum(member, request));

        if(albumImages != null)
            albumImageService.uploadAlbumImages(album, albumImages);

        return new AlbumIdResponse(album.getId());
    }

    @Override
    @Transactional
    public AlbumIdResponse updateAlbum(Member member, UUID albumId, AlbumUpdateRequest request, List<MultipartFile> albumImages) {
        Album album = loadEntity(albumId);

        if (!album.getWriter().getId().equals(member.getId())) {
            throw new RestApiException(ErrorCode.NO_AUTHORIZATION_ALBUM);
        }

        album.updateAlbum(request.getTitle(), request.getTitle(), request.getSemester());

        albumImageService.updateAlbumImages(album, albumImages);

        return new AlbumIdResponse(album.getId());
    }

    @Override
    @Transactional
    public AlbumIdResponse deleteAlbum(Member member, UUID albumId) {
        Album album = loadEntity(albumId);

        // 로그인한 member와 writer가 같지 않을 경우 삭제 불가능
        if (!album.getWriter().getId().equals(member.getId())) {
            // staff 역할을 가진 경우 삭제 가능
            if(member.getRole().getPriority() == Role.MEMBER.getPriority())
                throw new RestApiException(ErrorCode.NO_AUTHORIZATION_ALBUM);
        }

        albumImageService.deleteAlbumImages(album);
        album.delete();

        return new AlbumIdResponse(album.getId());
    }

    @Override
    @Transactional
    public AlbumDetailResponse showAlbumDetail(Member member, UUID albumId) {
        Album album = loadEntity(albumId);

        boolean isLike = false;
         Optional<AlbumHeart> albumHeart = albumHeartRepository.findByMemberAndAlbum(member, album);
         if(albumHeart.isPresent())
             isLike = albumHeart.get().isChecked();

         album.increaseHitCount();

         List<String> albumImages = albumImageService.findAlbumImages(album).stream()
                 .map(AlbumImage::getUrl).toList();

         return albumMapper.toAlbumDetailResponse(album, albumImages, isLike);
    }

    @Override
    public AlbumPagingResponse showAlbums(Member member, Pageable pageable) {
        return albumMapper.toAlbumPagingResponse(albumRepository.findAllAlbums(member, pageable));
    }

    @Override
    @Transactional
    public AlbumIdResponse toggleAlbumLike(Member member, UUID albumId) {
        Album album = loadEntity(albumId);

        AlbumHeart albumHeart = albumHeartRepository.findByMemberAndAlbum(member, album)
                .orElseGet(() -> {
                    AlbumHeart newHeart = albumHeartMapper.toAlbumHeartEntity(album, member);
                    albumHeartRepository.save(newHeart);
                    return newHeart;
                });

        albumHeart.toggleHeart();
        album.setHeartCount(albumHeart.isChecked());

        return new AlbumIdResponse(albumId);
    }

    @Override
    public Album loadEntity(UUID albumId) {
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new RestApiException(
                ErrorCode.EMPTY_ALBUM));
        return album;
    }
}
