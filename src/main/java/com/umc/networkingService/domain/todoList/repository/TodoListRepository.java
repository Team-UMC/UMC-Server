package com.umc.networkingService.domain.todoList.repository;

import com.umc.networkingService.domain.todoList.entity.ToDoList;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TodoListRepository extends JpaRepository<ToDoList, UUID> {
    @Query(value = "select b from ToDoList b where b.id = :todoListId and b.deletedAt is null")
    Optional<ToDoList> findById(@Param("todoListId") UUID todoListId);
}
