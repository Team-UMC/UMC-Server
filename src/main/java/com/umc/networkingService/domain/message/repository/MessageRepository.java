package com.umc.networkingService.domain.message.repository;

import com.umc.networkingService.domain.message.entity.Message;
import com.umc.networkingService.domain.message.entity.MessageRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Message findTopByMessageRoomOrderByCreatedAtDesc(MessageRoom messageRoom);

    //메시지룸 아이디로 메시지 리스트 최신순 조회, 페이징 20개씩
    Page<Message> findAllByMessageRoomIdOrderByCreatedAtDesc(UUID messageRoomId, PageRequest pageRequest);

}
