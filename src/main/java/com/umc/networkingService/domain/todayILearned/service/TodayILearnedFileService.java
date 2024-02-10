package com.umc.networkingService.domain.todayILearned.service;

import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearnedFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TodayILearnedFileService {
    void uploadTodayILearnedFiles(TodayILearned todayILearned, List<MultipartFile> files);
    List<TodayILearnedFile> findTodayILearnedFiles(TodayILearned todayILearned);
    void updateTodayILearnedFiles(TodayILearned todayILearned, List<MultipartFile> files);
    void deleteTodayILearnedFiles(TodayILearned todayILearned);

}
