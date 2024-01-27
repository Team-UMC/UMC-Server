package com.umc.networkingService.domain.todoList.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TodoListIdResponse {
        private UUID todoListId;
}
