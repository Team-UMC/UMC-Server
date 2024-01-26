package com.umc.networkingService.domain.todoList.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todoList.dto.request.TodoListCreateRequest;
import com.umc.networkingService.domain.todoList.dto.request.TodoListUpdateRequest;
import com.umc.networkingService.domain.todoList.dto.response.TodoListGetResponse;
import com.umc.networkingService.domain.todoList.dto.response.TodoListGetResponses;
import com.umc.networkingService.domain.todoList.dto.response.TodoListIdResponse;
import com.umc.networkingService.domain.todoList.service.TodoListService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "TodoList API", description = "TodoList 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/to-do-lists")
public class TodoListController {

    private final TodoListService todoListService;

    @Operation(summary = "투두리스트 작성 API", description = "투두리스트를 작성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @PostMapping
    public BaseResponse<TodoListIdResponse> createTodoList(@CurrentMember Member member,
                                                           @Valid @RequestBody TodoListCreateRequest request){
        return BaseResponse.onSuccess(todoListService.createTodoList(member, request));
    }

    @Operation(summary = "투두리스트 수정 API", description = "투두리스트를 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "TODOLIST001", description = "존재하지 않는 투두리스트입니다.")
    })
    @PostMapping("/update/{todoListId}")
    public BaseResponse<TodoListIdResponse> updateTodoList(@CurrentMember Member member,
                                                           @PathVariable("todoListId") UUID todoListId,
                                                           @Valid @RequestBody TodoListUpdateRequest request) {

        return BaseResponse.onSuccess(todoListService.updateTodoList(member, todoListId, request));
    }

    @Operation(summary = "투두리스트 완성 API", description = "투두리스트 완성 체크하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "TODOLIST001", description = "존재하지 않는 투두리스트입니다.")
    })
    @PostMapping("/{todoListId}")
    public BaseResponse<TodoListIdResponse> completeTodoList(@CurrentMember Member member, @PathVariable("todoListId") UUID todoListId) {

        return BaseResponse.onSuccess(todoListService.completeTodoList(member, todoListId));
    }

    @Operation(summary = "투두리스트 삭제 API", description = "투두리스트를 삭제하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "TODOLIST001", description = "존재하지 않는 투두리스트입니다.")
    })
    @DeleteMapping("/{todoListId}")
    public BaseResponse<TodoListIdResponse> deleteTodoList(@CurrentMember Member member, @PathVariable("todoListId") UUID todoListId) {

        return BaseResponse.onSuccess(todoListService.deleteTodoList(member, todoListId));
    }

    @Operation(summary = "투두리스트 조회 API", description = "요청하는 일자에 해당하는 투두리스트를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping
    public BaseResponse<TodoListGetResponses> showTodoList(@CurrentMember Member member, @RequestParam LocalDate date){
        return BaseResponse.onSuccess(todoListService.showTodoList(member, date));
    }
}
