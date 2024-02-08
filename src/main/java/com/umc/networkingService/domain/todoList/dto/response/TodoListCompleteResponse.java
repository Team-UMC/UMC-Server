package com.umc.networkingService.domain.todoList.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoListCompleteResponse {
    private UUID todoListId;
    private boolean isPointAcquired;
}
