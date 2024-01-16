package com.umc.networkingService.domain.todayILearned.entity;

import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "today_i_learned_file")
@NoArgsConstructor
@SQLRestriction("deleted_at is null")
public class TodayILearnedFile extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "today_i_learned_file_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private TodayILearned todayILearned;
}
