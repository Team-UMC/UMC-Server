package com.umc.networkingService.domain.schedule.dto.request;

import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.global.common.enums.Semester;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScheduleRequest {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateSchedule {
        private String title;
        private String content;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private List<Semester> semesterPermission;
        private HostType hostType;
        private String placeSetting;
    }
}
