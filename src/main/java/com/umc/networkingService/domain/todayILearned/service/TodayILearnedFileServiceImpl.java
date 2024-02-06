package com.umc.networkingService.domain.todayILearned.service;

import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import com.umc.networkingService.domain.todayILearned.mapper.TodayILearnedFileMapper;
import com.umc.networkingService.domain.todayILearned.repository.TodayILearnedFileRepository;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayILearnedFileServiceImpl implements TodayILearnedFileService {

    private final TodayILearnedFileRepository todayILearnedFileRepository;
    private final TodayILearnedFileMapper todayILearnedFileMapper;
    private final S3FileComponent s3FileComponent;

    // TodayILearned 파일 업로드
    @Override
    @Transactional
    public void uploadTodayILearnedFiles(TodayILearned todayILearned, List<MultipartFile> files) {
        files.forEach(file -> {
            todayILearnedFileRepository.save(todayILearnedFileMapper.toTodayILearnedFile(todayILearned,
                    s3FileComponent.uploadFile("TodayILearned", file)));
        });

    }
}
