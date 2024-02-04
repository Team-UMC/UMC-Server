package com.umc.networkingService.domain.todayILearned.repository;

import com.umc.networkingService.domain.todayILearned.entity.TodayILearnedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodayILearnedFileRepository extends JpaRepository<TodayILearnedFile, UUID> {
}
