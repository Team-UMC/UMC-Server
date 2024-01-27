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

    @Query(value = "SELECT s FROM Schedule s WHERE DATE_FORMAT(s.startDateTime, '%Y-%m') <= DATE_FORMAT(:date, '%Y-%m') AND DATE_FORMAT(:date, '%Y-%m') <= DATE_FORMAT(s.endDateTime, '%Y-%m')")
    List<Schedule> findSchedulesByYearAndMonth (
            @Param("yearMonth") LocalDate date
    );

}
