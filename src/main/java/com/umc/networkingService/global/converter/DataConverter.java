package com.umc.networkingService.global.converter;

import com.umc.networkingService.domain.member.entity.Member;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataConverter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static String convertToRelativeTimeFormat(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);

        long seconds = duration.toSeconds();
        if (seconds < 60) {
            return seconds + "초 전";
        }

        long minutes = duration.toMinutes();
        if (minutes < 60) {
            return minutes + "분 전";
        }

        long hours = duration.toHours();
        if (hours < 24) {
            return hours + "시간 전";
        }

        long days = duration.toDays();
        if (days < 8) {
            return days + "일 전";
        }

        return dateTime.format(DATE_FORMATTER);
    }

    public static String convertToWriter(Member member) {
        return member.getNickname() + "/" + member.getName();
    }
}
