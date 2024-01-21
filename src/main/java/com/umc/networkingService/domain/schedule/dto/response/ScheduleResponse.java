package com.umc.networkingService.domain.schedule.dto.response;

import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.global.common.enums.Semester;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScheduleResponse {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleInfo {
        private UUID id;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String hostType;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleInfoList {
        private List<ScheduleInfo> schedules;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleId {
        private UUID scheduleId;
    }


}
