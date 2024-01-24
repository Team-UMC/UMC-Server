package com.umc.networkingService.domain.friend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendInquiryByStatusResponse {
    private List<FriendInfoResponse> friends;
    private boolean hasNext;
}
