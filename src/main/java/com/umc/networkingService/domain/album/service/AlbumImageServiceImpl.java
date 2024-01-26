package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.entity.Album;
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
    public void uploadAlbumImages(Album album, List<MultipartFile> imageFiles) {
        imageFiles.forEach(imageFile -> albumImageRepository.save(albumImageMapper
                .toAlbumImage(album, s3FileComponent.uploadFile("Album", imageFile))));
    }
}
