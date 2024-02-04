package com.umc.networkingService.domain.todayILearned.service;

import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TodayILearnedFileService {
    void uploadTodayILearnedFiles(TodayILearned todayILearned, List<MultipartFile> files);
}
