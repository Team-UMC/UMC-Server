package com.umc.networkingService.domain.proposal.entity;

import com.umc.networkingService.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Proposal extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    //member 테이블과 연결
    // private Member writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    //@Column(nullable = false)
    //private List<String> images;
}
