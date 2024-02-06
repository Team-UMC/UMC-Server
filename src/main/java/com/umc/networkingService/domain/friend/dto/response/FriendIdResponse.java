package com.umc.networkingService.domain.friend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FriendIdResponse {
    private UUID friendId;
}
