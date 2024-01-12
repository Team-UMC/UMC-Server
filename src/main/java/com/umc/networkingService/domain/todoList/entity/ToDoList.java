package com.umc.networkingService.domain.todoList.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class ToDoList extends BaseEntity {

    @Id
    @UuidGenerator
    @Column
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private boolean isCompleted;

}
