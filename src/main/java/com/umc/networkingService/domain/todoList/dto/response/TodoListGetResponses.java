package com.umc.networkingService.domain.todoList.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class TodoListGetResponses {
    private List<TodoListGetResponse> todoLists;
}
