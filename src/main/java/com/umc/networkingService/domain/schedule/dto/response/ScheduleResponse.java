package com.umc.networkingService.domain.schedule.dto.response;

import com.umc.networkingService.domain.schedule.entity.Schedule;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScheduleResponse {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CalendarInfoForDay {
        private UUID id;
        private String title;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String hostType;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CalendarInfoForMonth {
        private List<CalendarInfoForMonth> calendarInfos;
    }
}
