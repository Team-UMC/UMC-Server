package com.umc.networkingService.domain.todoList.controller;


import com.umc.networkingService.domain.todoList.entity.ToDoList;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoListControllerTestConfig {

    protected ToDoList todoList;
/*
    @BeforeEach
    public void setUp(){
        todoList = createTodoLIst();
    }
*/
}
