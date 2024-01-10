package com.umc.networkingService.domain.friend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Getter
@RequiredArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

//    member 테이블과 연결
//    private UUID sender;

//    member 테이블과 연결
//    private UUID receiver;
}
