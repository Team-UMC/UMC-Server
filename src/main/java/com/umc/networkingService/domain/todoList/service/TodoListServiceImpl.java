package com.umc.networkingService.domain.todoList.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todoList.dto.request.TodoListCreateRequest;
import com.umc.networkingService.domain.todoList.dto.request.TodoListUpdateRequest;
import com.umc.networkingService.domain.todoList.dto.response.TodoListGetResponse;
import com.umc.networkingService.domain.todoList.dto.response.TodoListIdResponse;
import com.umc.networkingService.domain.todoList.entity.ToDoList;
import com.umc.networkingService.domain.todoList.mapper.TodoListCompleteMapper;
import com.umc.networkingService.domain.todoList.mapper.TodoListCreateMapper;
import com.umc.networkingService.domain.todoList.mapper.TodoListUpdateMapper;
import com.umc.networkingService.domain.todoList.repository.TodoListRepository;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.umc.networkingService.global.common.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoListServiceImpl implements TodoListService{

    private final TodoListRepository todoListRepository;

    private final TodoListCreateMapper todoListCreateMapper;
    private final TodoListUpdateMapper todoListUpdateMapper;
    private final TodoListCompleteMapper todoListCompleteMapper;
    @Override
    public TodoListIdResponse createTodoList(Member member, TodoListCreateRequest todoListRequest){
        ToDoList todoList = todoListCreateMapper.createTodoListToTodoList(member, todoListRequest);

        ToDoList savedtodolist = todoListRepository.save(todoList);

        return new TodoListIdResponse(savedtodolist.getId());
    }

    @Override
    public TodoListIdResponse updateTodoList(Member member, UUID todoListId, TodoListUpdateRequest todoListUpdateRequest){
        ToDoList todoList = todoListRepository.findById(todoListId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_TODOLIST));
        ToDoList updatetodoList = todoListUpdateMapper.updateTodoListToTodoList(member, todoList, todoListUpdateRequest);

        todoListRepository.save(updatetodoList);

        return new TodoListIdResponse((updatetodoList.getId()));
    }

    @Override
    public TodoListIdResponse completeTodoList(Member member, UUID todoListId){
        ToDoList todoList = todoListRepository.findById(todoListId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_TODOLIST));
        ToDoList completetodoList = todoListCompleteMapper.completeTodoListToTodoList(member, todoList);

        todoListRepository.save(completetodoList);

        return new TodoListIdResponse((completetodoList.getId()));
    }


    @Override
    public TodoListIdResponse deleteTodoList(Member member, UUID todoListId){
        ToDoList todoList = todoListRepository.findById(todoListId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_TODOLIST));

        todoList.delete();

        return new TodoListIdResponse(todoList.getId());
    }

    @Override
    public TodoListGetResponse showTodoList(Member member){
        return todoList
    }

}
