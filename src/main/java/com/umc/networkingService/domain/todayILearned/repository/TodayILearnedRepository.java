package com.umc.networkingService.domain.todayILearned.repository;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todayILearned.entity.TodayILearned;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TodayILearnedRepository extends JpaRepository<TodayILearned, UUID> {

    @Query(value = "SELECT t FROM TodayILearned t WHERE (t.writer = :writer AND DATE(t.createdAt) = :date)")
    List<TodayILearned> findTodayILearnedByWriterAndCreateDate(@Param("writer") Member writer,
                                                              @Param("date") LocalDate date);

    List<TodayILearned> findAllByWriterAndCreatedAtBetween(Member writer, LocalDateTime createdAt, LocalDateTime createdAt2);
}
