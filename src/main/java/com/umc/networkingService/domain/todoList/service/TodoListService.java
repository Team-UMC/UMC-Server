package com.umc.networkingService.domain.todoList.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todoList.dto.request.TodoListCreateRequest;
import com.umc.networkingService.domain.todoList.dto.request.TodoListUpdateRequest;
import com.umc.networkingService.domain.todoList.dto.response.TodoListGetResponses;
import com.umc.networkingService.domain.todoList.dto.response.TodoListIdResponse;

import java.time.LocalDate;
import java.util.UUID;

public interface TodoListService {
    TodoListIdResponse createTodoList(Member member, TodoListCreateRequest request);

    TodoListIdResponse deleteTodoList(Member member, UUID todoListId);

    TodoListGetResponses showTodoList(Member member, LocalDate date);

    TodoListIdResponse completeTodoList(Member member, UUID todoListId);

    TodoListIdResponse updateTodoList(Member member, UUID todoListId, TodoListUpdateRequest request);

}
