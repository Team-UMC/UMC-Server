package com.umc.networkingService.domain.todoList.mapper;

import com.umc.networkingService.domain.todoList.dto.response.TodoListGetResponse;
import com.umc.networkingService.domain.todoList.entity.ToDoList;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TodoListShowMapper {

    public TodoListGetResponse showTodoListToTodoList(ToDoList toDoList){
        return TodoListGetResponse.builder()
                .title(toDoList.getTitle())
                .deadline(toDoList.getDeadline())
                .isCompleted()
                .build();
    }
}
