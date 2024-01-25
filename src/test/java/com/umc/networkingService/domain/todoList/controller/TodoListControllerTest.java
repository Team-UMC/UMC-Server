package com.umc.networkingService.domain.todoList.controller;

import com.umc.networkingService.domain.todoList.dto.request.TodoListCreateRequest;
import com.umc.networkingService.domain.todoList.dto.response.TodoListIdResponse;
import com.umc.networkingService.domain.todoList.mapper.TodoListMapper;
import com.umc.networkingService.domain.todoList.service.TodoListService;
import com.umc.networkingService.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

@DisplayName("TodoList 컨트롤러의")
public class TodoListControllerTest extends ControllerTestConfig {
   /*
    @Autowired
    private TodoListMapper todoListMapper;

    @MockBean
    private TodoListService todoListService;

    @DisplayName("투두리스트 생성 API 테스트")
    @Test
    public void createTodoListTest() throws Exception{
        TodoListCreateRequest request = TodoListCreateRequest.builder()
                .title("타이틀")
                .deadline(LocalDateTime.parse("yyyy-MM-dd HH:mm:ss"))
                .build();

        TodoListIdResponse response = new TodoListIdResponse(TodoList)
    }

    */
}
