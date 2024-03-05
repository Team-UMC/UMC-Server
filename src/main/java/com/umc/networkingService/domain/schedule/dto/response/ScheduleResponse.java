package com.umc.networkingService.domain.schedule.dto.response;

import com.umc.networkingService.domain.board.entity.HostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ScheduleResponse {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleInfoSummaryInCalendar {
        private UUID scheduleId;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String hostType;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleInfoSummariesInCalendar {
        private List<ScheduleInfoSummaryInCalendar> schedules;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleInfoSummary {
        private UUID scheduleId;
        private LocalDateTime startDateTime;
        private LocalDateTime endDatetime;
        private String title;
        private HostType hostType;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleInfoSummaryLists {
        private List<ScheduleInfoSummary> campusSchedules;
        private List<ScheduleInfoSummary> branchSchedules;
        private List<ScheduleInfoSummary> centerSchedules;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleId {
        private UUID scheduleId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ScheduleInfo {
        private UUID scheduleId;
        private String writerName;
        private String writerNickname;
        private String title;
        private String content;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String placeSetting;
        private HostType hostType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleInfos {
        private List<ScheduleInfo> schedules;
    }
}