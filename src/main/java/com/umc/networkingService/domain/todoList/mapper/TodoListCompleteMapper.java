package com.umc.networkingService.domain.todoList.mapper;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todoList.dto.request.TodoListCreateRequest;
import com.umc.networkingService.domain.todoList.entity.ToDoList;
import org.springframework.stereotype.Component;

@Component
public class TodoListCompleteMapper {
    //수정이랑 완료에도 .writer를 사용해야하는지!!!
    public ToDoList completeTodoListToTodoList(Member member, ToDoList todoList){
        todoList.setCompleted(true);
        todoList.setWriter(member);

        return todoList;
    }
}
