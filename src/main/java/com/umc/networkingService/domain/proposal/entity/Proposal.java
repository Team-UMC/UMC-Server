package com.umc.networkingService.domain.proposal.entity;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ElementCollection
    private List<String> images = new ArrayList<>();


}
