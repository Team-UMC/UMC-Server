package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumImage;
import com.umc.networkingService.domain.album.mapper.AlbumImageMapper;
import com.umc.networkingService.domain.album.repository.AlbumImageRepository;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumImageServiceImpl implements AlbumImageService{

    private final AlbumImageRepository albumImageRepository;
    private final AlbumImageMapper albumImageMapper;
    private final S3FileComponent s3FileComponent;

    @Override
    @Transactional
    public void createAlbumImages(Album album, List<MultipartFile> albumImages) {

        albumImages.forEach(albumImage -> albumImageRepository.save(
                        albumImageMapper.toAlbumImage(
                                album,
                                s3FileComponent.uploadFile("album", albumImage))));
    }

    @Override
    @Transactional
    public void updateAlbumImages(Album album, List<MultipartFile> albumImages) {

        // 기존 이미지 삭제
        deleteAlbumImages(album);

        if (albumImages != null)
            createAlbumImages(album, albumImages);
    }

    @Override
    public void deleteAlbumImages(Album album) {
        List<AlbumImage> albumImages = findAlbumImages(album);

        albumImages.forEach(albumImage -> {
            s3FileComponent.deleteFile(albumImage.getUrl());
            albumImageRepository.deleteById(albumImage.getId());
        });
    }

    @Override
    public List<AlbumImage> findAlbumImages(Album album) {
        return albumImageRepository.findAllByAlbum(album);
    }

    @Override
    public String findThumbnailImage(Album album) {
        return findAlbumImages(album).stream()
                .filter(file -> isImageFile(file.getUrl()))
                .findFirst()
                .map(AlbumImage::getUrl)
                .orElse(null);
    }

    // 특정 앨범 이미지 개수 조회 함수
    @Override
    public int countAlbumImages(Album album) {
        return albumImageRepository.countAllByAlbum(album);
    }

    public static boolean isImageFile(String url) {
        String lowerCaseUrl = url.toLowerCase();

        return lowerCaseUrl.endsWith(".jpg") || lowerCaseUrl.endsWith(".jpeg")
                || lowerCaseUrl.endsWith(".png") || lowerCaseUrl.endsWith(".gif");
    }
}
