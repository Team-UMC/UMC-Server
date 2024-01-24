package com.umc.networkingService.domain.todoList.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoListGetResponse {
    private String title;
    private LocalDateTime deadline;
    private boolean isCompleted;
}
