package com.umc.networkingService.domain.todayILearned.repository;

import com.umc.networkingService.domain.todayILearned.entity.TodayILearnedFile;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodayILearnedFileRepository extends JpaRepository<TodayILearnedFile, UUID> {
}
