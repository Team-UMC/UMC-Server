package com.umc.networkingService.domain.schedule.repository;

import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.schedule.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    @Query(value = "SELECT s FROM Schedule s WHERE (DATE_FORMAT(s.startDateTime, '%Y-%m') <= DATE_FORMAT(:date, '%Y-%m') AND DATE_FORMAT(:date, '%Y-%m') <= DATE_FORMAT(s.endDateTime, '%Y-%m')) AND s.deletedAt IS NULL")
    List<Schedule> findSchedulesByYearAndMonth (
            @Param("yearMonth") LocalDate date
    );

    @Query(value = "SELECT s FROM Schedule s WHERE s.hostType = 1 AND (MONTH(s.startDateTime) <= :month AND :month <= MONTH(s.endDateTime) AND s.deletedAt IS NULL) "  +
            "UNION " +
            "SELECT s FROM Schedule s WHERE s.hostType = 2 AND (MONTH(s.startDateTime) <= :month AND :month <= MONTH(s.endDateTime) AND s.deletedAt IS NULL) " +
            "UNION " +
            "SELECT s FROM Schedule s WHERE s.hostType = 3 AND (MONTH(s.startDateTime) <= :month AND :month <= MONTH(s.endDateTime) AND s.deletedAt IS NULL)")
    List<List<Schedule>> findByHostTypes (
            @Param("month") Long month
    );

}
