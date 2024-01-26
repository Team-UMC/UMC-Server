package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.entity.Album;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AlbumImageService {
    void uploadAlbumImages(Album album, List<MultipartFile> imageFiles);
}
