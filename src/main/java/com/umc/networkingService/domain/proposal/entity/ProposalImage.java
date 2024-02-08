package com.umc.networkingService.domain.proposal.entity;

import com.umc.networkingService.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
@Builder
@AllArgsConstructor
public class ProposalImage extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "proposal_image_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="proposal_id")
    private Proposal proposal;

    @Column(nullable = false)
    private String url;
}
