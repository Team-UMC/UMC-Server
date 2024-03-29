package com.umc.networkingService.domain.todayILearned.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.todayILearned.dto.requeest.TodayILearnedRequest.TodayILearnedUpdate;
import com.umc.networkingService.global.common.base.BaseEntity;
import com.umc.networkingService.global.common.enums.Part;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Builder
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("deleted_at is null")
@Table(name = "today_i_learned")
public class TodayILearned extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "today_i_learned_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member writer;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Part part;

    public void updateTodayILearned(TodayILearnedUpdate request) {
        this.title = request.getTitle();
        this.subtitle = request.getSubTitle();
        this.content = request.getContent();
        this.part = request.getPart();

    }

}
