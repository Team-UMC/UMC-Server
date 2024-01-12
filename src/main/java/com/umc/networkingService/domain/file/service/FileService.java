package com.umc.networkingService.domain.file.service;


import com.umc.networkingService.domain.file.dto.response.FileCreateResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileCreateResponse createFile(String domain, final MultipartFile file);

    void deleteFile(String fileUrl);
}
