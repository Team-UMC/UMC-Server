package com.umc.networkingService.domain.todoList.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoListGetResponse {
    private UUID todoListId;
    private String title;
    private LocalDateTime deadline;
    private boolean isCompleted;
}
