package com.umc.networkingService.domain.exceptionTest;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TempRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempTestRequest {
        @NotBlank(message = "testString은 필수입니다.")
        private String testString;
    }
}
