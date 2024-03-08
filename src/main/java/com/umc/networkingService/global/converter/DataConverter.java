package com.umc.networkingService.global.converter;

import com.umc.networkingService.domain.member.entity.Member;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataConverter {
    public static String convertToRelativeTimeFormat(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);

        if (duration.toHours() < 24) {
            return duration.toHours() + "시간 전";
        } else if (duration.toDays() < 8) {
            return duration.toDays() + "일 전";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            return dateTime.format(formatter);
        }
    }

    public static String convertToWriter(Member member) {
        return member.getNickname() + "/" + member.getName();
    }
}
