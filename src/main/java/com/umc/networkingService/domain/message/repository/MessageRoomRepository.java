package com.umc.networkingService.domain.message.repository;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.message.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, UUID> {
    boolean existsByReceiverAndSenderAndIsAnonymous(Member receiver, Member sender, boolean isAnonymous);
    List<MessageRoom> findAllByReceiverOrSender(Member receiver, Member sender);
}
