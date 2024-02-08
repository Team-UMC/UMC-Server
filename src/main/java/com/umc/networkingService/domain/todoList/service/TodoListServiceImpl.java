package com.umc.networkingService.domain.todoList.service;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.todoList.dto.request.TodoListCreateRequest;
import com.umc.networkingService.domain.todoList.dto.request.TodoListUpdateRequest;
import com.umc.networkingService.domain.todoList.dto.response.TodoListCompleteResponse;
import com.umc.networkingService.domain.todoList.dto.response.TodoListGetResponses;
import com.umc.networkingService.domain.todoList.dto.response.TodoListIdResponse;
import com.umc.networkingService.domain.todoList.entity.ToDoList;
import com.umc.networkingService.domain.todoList.mapper.TodoListMapper;
import com.umc.networkingService.domain.todoList.repository.TodoListRepository;
import com.umc.networkingService.global.common.exception.code.GlobalErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.ToDoListErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoListServiceImpl implements TodoListService{

    private final TodoListRepository todoListRepository;
    private final TodoListMapper todoListMapper;

    private final MemberService memberService;

    @Override
    public TodoListIdResponse createTodoList(Member member, TodoListCreateRequest todoListRequest){
        ToDoList todoList = todoListMapper.createTodoList(member, todoListRequest);

        ToDoList savedtodolist = todoListRepository.save(todoList);

        return new TodoListIdResponse(savedtodolist.getId());
    }

    @Override
    @Transactional
    public TodoListIdResponse updateTodoList(Member member, UUID todoListId, TodoListUpdateRequest request){
        ToDoList todoList = todoListRepository.findById(todoListId).orElseThrow(() -> new RestApiException(ToDoListErrorCode.EMPTY_TODOLIST));

        // 투두 리스트에 권한이 없는 경우 예외처리
        if (!todoList.getWriter().getId().equals(member.getId())) {
            throw new RestApiException(ToDoListErrorCode.NO_AUTHORIZATION_TODOLIST);
        }

        // 이미 완료한 투두 리스트인 경우 예외처리
        if (todoList.isCompleted())
            throw new RestApiException(ToDoListErrorCode.ALREADY_COMPLETE_TODOLIST);

        todoList.updateTodoList(request.getTitle(), request.getDeadline());

        return new TodoListIdResponse((todoList.getId()));
    }

    @Override
    @Transactional
    public TodoListCompleteResponse completeTodoList(Member loginMember, UUID todoListId){

        Member member = memberService.loadEntity(loginMember.getId());

        ToDoList todoList = todoListRepository.findById(todoListId)
                .orElseThrow(() -> new RestApiException(ToDoListErrorCode.EMPTY_TODOLIST));

        // 이미 완료한 투두 리스트인 경우 예외처리
        if (todoList.isCompleted())
            throw new RestApiException(ToDoListErrorCode.ALREADY_COMPLETE_TODOLIST);

        // 투두 리스트에 권한이 없는 경우 예외처리
        if (!todoList.getWriter().getId().equals(member.getId()))
            throw new RestApiException(ToDoListErrorCode.NO_AUTHORIZATION_TODOLIST);

        // deadline이 지난 투두리스트인 경우 예외처리
        if (!todoList.getDeadline().isBefore(LocalDateTime.now()))
            throw new RestApiException(ToDoListErrorCode.EXPIRED_TODOLIST);

        todoList.completeTodoList();

        return new TodoListCompleteResponse(todoList.getId(), validatePointAcquired(member, todoList));
    }

    @Override
    @Transactional
    public TodoListIdResponse deleteTodoList(Member member, UUID todoListId){
        ToDoList todoList = todoListRepository.findById(todoListId).orElseThrow(() -> new RestApiException(ToDoListErrorCode.EMPTY_TODOLIST));

        if (!todoList.getWriter().getId().equals(member.getId())) {
            throw new RestApiException(ToDoListErrorCode.NO_AUTHORIZATION_TODOLIST);
        }

        todoList.delete();

        return new TodoListIdResponse(todoList.getId());
    }

    @Override
    public TodoListGetResponses showTodoList(Member member, LocalDate date){
        List<ToDoList> todoLists = todoListRepository.findAllByWriterAndDeadline(member, date);

        return new TodoListGetResponses(
                todoLists.stream()
                        .map(todoListMapper::toTodoListGetResponse)
                        .toList());
    }

    // 포인트 획득 가능 여부 확인 함수
    private boolean validatePointAcquired(Member member, ToDoList toDoList) {
        List<ToDoList> todoLists = todoListRepository.findAllByWriterAndUpdatedAtBetweenAndIsCompleted(
                member,
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN),
                LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX),
                true
        );

        todoLists.sort(Comparator.comparing(ToDoList::getUpdatedAt));

        int index = todoLists.indexOf(toDoList);

        // 투두리스트를 찾지 못한 경우
        if (index == -1) throw new RestApiException(ToDoListErrorCode.EMPTY_TODOLIST);

        if (index < 3) {
            member.addRemainPoint(1L);
            return true;
        }
        return false;
    }
}