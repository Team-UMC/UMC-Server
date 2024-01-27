package com.umc.networkingService.domain.todoList.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoListUpdateRequest {

    private LocalDateTime deadline;

    @NotBlank(message = "할일을 입력해주세요")
    private String title;
}
