package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AlbumImageService {
    void createAlbumImages(Album album, List<MultipartFile> albumImages);
    void updateAlbumImages(Album album, List<MultipartFile> albumImages);
    void deleteAlbumImages(Album album);
    List<AlbumImage> findAlbumImages(Album album);
    String findThumbnailImage(Album album);
    int countAlbumImages(Album album);
}
