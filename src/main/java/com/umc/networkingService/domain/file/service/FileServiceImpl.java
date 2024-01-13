package com.umc.networkingService.domain.file.service;

import com.umc.networkingService.domain.file.dto.response.FileCreateResponse;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {

    private final S3FileComponent s3FileComponent;

    @Transactional
    public FileCreateResponse createFile(String domain , final MultipartFile file) {
        String imageUrl = s3FileComponent.uploadFile(domain, file);
        return new FileCreateResponse(imageUrl);
    }

    @Override
    public void deleteFile(String fileUrl) {
        s3FileComponent.deleteFile(fileUrl);
    }


}
