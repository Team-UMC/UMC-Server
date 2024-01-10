package com.umc.networkingService.domain.todoList.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

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
