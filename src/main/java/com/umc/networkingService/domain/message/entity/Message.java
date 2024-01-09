package com.umc.networkingService.domain.message.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Getter
@RequiredArgsConstructor
public class Message {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

//    MessageRoom 테이블과 연결
//    @Column(nullable = false)
//    private UUID messageRoom;

    private String content;

    @Column(nullable = false)
    private Boolean isSender;

}
