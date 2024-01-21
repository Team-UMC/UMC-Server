package com.umc.networkingService.domain.schedule.repository;

import com.umc.networkingService.domain.schedule.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    @Query(value = "SELECT s FROM Schedule s WHERE (MONTH(s.startDateTime) <= :month AND :month <= MONTH(s.endDateTime)) AND s.deletedAt IS NULL")
    List<Schedule> findSchedulesByMonth(
            @Param("month") Long month
    );


}
