package com.umc.networkingService.domain.proposal.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.dto.request.ProposalUpdateRequest;
import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Proposal extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "proposal_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="member_id")
    private Member writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ColumnDefault("0")
    private int commentCount;

    public void update(ProposalUpdateRequest request){
        this.writer = request.getWriter();
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }
}
