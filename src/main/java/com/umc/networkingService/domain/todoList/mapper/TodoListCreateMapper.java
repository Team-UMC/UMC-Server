package com.umc.networkingService.domain.todoList.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todoList.dto.request.TodoListCreateRequest;
import com.umc.networkingService.domain.todoList.dto.request.TodoListUpdateRequest;
import com.umc.networkingService.domain.todoList.entity.ToDoList;
import org.springframework.stereotype.Component;

@Component
public class TodoListCreateMapper {
    public ToDoList createTodoListToTodoList(Member member, TodoListCreateRequest request){
        return ToDoList.builder()
                .writer(member)
                .deadline(request.getDeadline())
                .title(request.getTitle())
                .isCompleted(false)
                .build();
    }
}
