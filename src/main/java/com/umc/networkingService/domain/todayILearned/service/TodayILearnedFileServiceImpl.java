package com.umc.networkingService.domain.todayILearned.service;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardFile;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearnedFile;
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

    @Override
    @Transactional
    public void updateTodayILearnedFiles(TodayILearned todayILearned, List<MultipartFile> files) {

        List<TodayILearnedFile> todayILearnedFiles = findTodayILearnedFiles(todayILearned);

        todayILearnedFiles.forEach(file -> {
            s3FileComponent.deleteFile(file.getUrl());
            todayILearnedFileRepository.deleteById(file.getId());
        });

        if (files != null)
            uploadTodayILearnedFiles(todayILearned, files);
    }

    @Override
    @Transactional
    public void deleteTodayILearnedFiles(TodayILearned todayILearned) {
        List<TodayILearnedFile> todayILearnedFiles = findTodayILearnedFiles(todayILearned);

        todayILearnedFiles.forEach(TodayILearnedFile::delete);
    }


    @Override
    public List<TodayILearnedFile> findTodayILearnedFiles(TodayILearned todayILearned) {
        return todayILearnedFileRepository.findAllByTodayILearned(todayILearned);
    }


}
