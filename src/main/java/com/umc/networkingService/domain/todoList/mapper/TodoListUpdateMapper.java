package com.umc.networkingService.domain.todoList.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todoList.dto.request.TodoListUpdateRequest;
import com.umc.networkingService.domain.todoList.entity.ToDoList;
import org.springframework.stereotype.Component;

@Component
public class TodoListUpdateMapper {
    public ToDoList updateTodoListToTodoList(Member member, ToDoList todoList, TodoListUpdateRequest request){
        todoList.setTitle(request.getTitle());
        todoList.setDeadline(request.getDeadline());
        todoList.setCompleted(false);
        todoList.setWriter(member);
        return todoList;
    }
}
