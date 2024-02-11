package com.umc.networkingService.domain.todoList.repository;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todoList.entity.ToDoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoListRepository extends JpaRepository<ToDoList, UUID> {
    Optional<ToDoList> findById(UUID todoListId);
    @Query("SELECT td FROM ToDoList td WHERE td.writer = :writer AND DATE(td.deadline) = :date")
    List<ToDoList> findAllByWriterAndDeadline(Member writer, LocalDate date);
    List<ToDoList> findAllByWriterAndUpdatedAtBetweenAndIsCompleted(Member writer, LocalDateTime startTime,
                                                                    LocalDateTime endTime, boolean isCompleted);
}
