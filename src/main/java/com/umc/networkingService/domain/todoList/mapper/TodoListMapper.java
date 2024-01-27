package com.umc.networkingService.domain.todoList.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todoList.dto.request.TodoListCreateRequest;
import com.umc.networkingService.domain.todoList.dto.response.TodoListGetResponse;
import com.umc.networkingService.domain.todoList.entity.ToDoList;
import org.springframework.stereotype.Component;

@Component
public class TodoListMapper {

    public ToDoList createTodoList(Member member, TodoListCreateRequest request){
        return ToDoList.builder()
                .writer(member)
                .deadline(request.getDeadline())
                .title(request.getTitle())
                .isCompleted(false)
                .build();
    }

    public TodoListGetResponse toTodoListGetResponse(ToDoList toDoList){
        return TodoListGetResponse.builder()
                .todoListId(toDoList.getId())
                .title(toDoList.getTitle())
                .deadline(toDoList.getDeadline())
                .isCompleted(toDoList.isCompleted())
                .build();
    }

}
