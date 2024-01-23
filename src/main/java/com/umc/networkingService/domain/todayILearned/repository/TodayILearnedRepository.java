package com.umc.networkingService.domain.todayILearned.repository;

import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodayILearnedRepository extends JpaRepository<TodayILearned, UUID> {
}
