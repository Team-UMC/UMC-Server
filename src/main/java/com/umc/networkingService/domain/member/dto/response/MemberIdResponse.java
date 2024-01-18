package com.umc.networkingService.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class MemberIdResponse {
    private UUID memberId;
}
