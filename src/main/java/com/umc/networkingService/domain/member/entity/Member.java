package com.umc.networkingService.domain.member.entity;

import com.umc.networkingService.global.common.BaseEntity;
import com.umc.networkingService.global.common.Part;
import com.umc.networkingService.global.common.Role;
import com.umc.networkingService.global.common.Semester;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private UUID university;

    @Column(nullable = false)
    private UUID branch;

    private String profileImage;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long remainPoint;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String name;

    private String statusMessage;

    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false)
    @Builder.Default
    private List<Part> part = new ArrayList<>();

    @Column(nullable = false)
    private List<Semester> semester;

    @Column(nullable = false)
    private Role role;

    private Position position;

    private String gitNickname;

    private  String notionLink;
}
