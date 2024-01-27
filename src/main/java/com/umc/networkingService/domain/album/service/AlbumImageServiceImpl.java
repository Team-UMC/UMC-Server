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
    public void uploadAlbumImages(Album album, List<MultipartFile> albumImages) {
        albumImages.forEach(albumImage -> albumImageRepository.save(albumImageMapper
                .toAlbumImage(album, s3FileComponent.uploadFile("Album", albumImage))));
    }

    @Override
    @Transactional
    public void updateAlbumImages(Album album, List<MultipartFile> albumImages) {
        List<AlbumImage> Images = findAlbumImages(album);

        Images.forEach(albumImage -> {
            s3FileComponent.deleteFile(albumImage.getUrl());
            albumImageRepository.deleteById(albumImage.getId());
        });

        if(Images != null)
            uploadAlbumImages(album, albumImages);
    }

    @Override
    @Transactional
    public void deleteAlbumImages(Album album) {
        List<AlbumImage> albumImages = findAlbumImages(album);

        albumImages.forEach(AlbumImage::delete);
    }

    @Override
    public List<AlbumImage> findAlbumImages(Album album) {
        return albumImageRepository.findAllByAlbum(album);
    }
}
