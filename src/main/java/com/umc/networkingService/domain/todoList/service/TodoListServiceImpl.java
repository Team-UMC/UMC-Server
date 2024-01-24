package com.umc.networkingService.domain.todoList.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todoList.dto.request.TodoListCreateRequest;
import com.umc.networkingService.domain.todoList.dto.request.TodoListUpdateRequest;
import com.umc.networkingService.domain.todoList.dto.response.TodoListGetResponses;
import com.umc.networkingService.domain.todoList.dto.response.TodoListIdResponse;
import com.umc.networkingService.domain.todoList.entity.ToDoList;
import com.umc.networkingService.domain.todoList.mapper.TodoListCreateMapper;
import com.umc.networkingService.domain.todoList.mapper.TodoListShowMapper;
import com.umc.networkingService.domain.todoList.repository.TodoListRepository;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.umc.networkingService.global.common.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoListServiceImpl implements TodoListService{

    private final TodoListRepository todoListRepository;

    private final TodoListCreateMapper todoListCreateMapper;
    private final TodoListShowMapper todoListShowMapper;

    @Override
    public TodoListIdResponse createTodoList(Member member, TodoListCreateRequest todoListRequest){
        ToDoList todoList = todoListCreateMapper.createTodoListToTodoList(member, todoListRequest);

        ToDoList savedtodolist = todoListRepository.save(todoList);

        return new TodoListIdResponse(savedtodolist.getId());
    }

    @Override
    @Transactional
    public TodoListIdResponse updateTodoList(Member member, UUID todoListId, TodoListUpdateRequest request){
        ToDoList todoList = todoListRepository.findById(todoListId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_TODOLIST));

        todoList.updateTodoList(request.getTitle(), request.getDeadline());

        return new TodoListIdResponse((todoList.getId()));
    }

    @Override
    @Transactional
    public TodoListIdResponse completeTodoList(Member member, UUID todoListId){
        ToDoList todoList = todoListRepository.findById(todoListId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_TODOLIST));

        todoList.completeTodoList();

        return new TodoListIdResponse(todoList.getId());
    }

    @Override
    public TodoListIdResponse deleteTodoList(Member member, UUID todoListId){
        ToDoList todoList = todoListRepository.findById(todoListId).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_TODOLIST));

        todoList.delete();

        return new TodoListIdResponse(todoList.getId());
    }

    @Override
    public TodoListGetResponses showTodoList(Member member, LocalDate date){
        List<ToDoList> todoLists = todoListRepository.findAllByWriterAndDeadline(member, date);

        return new TodoListGetResponses(
                todoLists.stream()
                        .map(todoListShowMapper::showTodoListToTodoList)
                        .toList());
    }
}