package com.umc.networkingService.domain.message.service;

import com.umc.networkingService.domain.message.mapper.MessageMapper;
import com.umc.networkingService.domain.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements  MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

}
