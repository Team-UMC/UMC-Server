package com.umc.networkingService.domain.schedule.repository;

import com.umc.networkingService.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
