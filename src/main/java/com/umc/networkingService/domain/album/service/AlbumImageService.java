package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumImage;
import com.umc.networkingService.domain.album.repository.AlbumImageRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AlbumImageService {
    void uploadAlbumImages(Album album, List<MultipartFile> albumImages);

    void updateAlbumImages(Album album, List<MultipartFile> albumImages);

    List<AlbumImage> findAlbumImages(Album album);
}
