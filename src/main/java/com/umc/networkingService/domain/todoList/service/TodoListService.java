package com.umc.networkingService.domain.todoList.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todoList.dto.request.TodoListCreateRequest;
import com.umc.networkingService.domain.todoList.dto.request.TodoListUpdateRequest;
import com.umc.networkingService.domain.todoList.dto.response.TodoListGetResponse;
import com.umc.networkingService.domain.todoList.dto.response.TodoListIdResponse;

import java.util.UUID;

public interface TodoListService {
    TodoListIdResponse createTodoList(Member member, TodoListCreateRequest todoListCreateRequest);

    TodoListIdResponse updateTodoList(Member member, UUID todoListId, TodoListUpdateRequest todoListUpdateRequest);

    TodoListIdResponse completeTodoList(Member member, UUID todoListId);

    /*
    TodoListIdResponse deleteTodoList(Member member, UUID todoListId);

    TodoListGetResponse showTodoList(Member member);

     */
}
