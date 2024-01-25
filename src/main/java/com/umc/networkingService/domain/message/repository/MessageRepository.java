package com.umc.networkingService.domain.message.repository;

import com.umc.networkingService.domain.message.entity.Message;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
