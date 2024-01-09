package com.umc.networkingService.domain.messageRoom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Getter
@RequiredArgsConstructor
public class MessageRoom {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2")
    @Column
    private UUID id;

//    member 테이블과 연결
//    @Column(nullable = false)
//    private UUID sender;

//    member 테이블과 연결
//    @Column(nullable = false)
//    private UUID receiver;

    @Column(nullable = false)
    private Boolean isAnonymous;
}
