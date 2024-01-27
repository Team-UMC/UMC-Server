package com.umc.networkingService.domain.invite.mapper;

import com.umc.networkingService.domain.invite.dto.response.InviteInquiryMineResponse;
import com.umc.networkingService.domain.invite.entity.Invite;
import org.springframework.stereotype.Component;

@Component
public class InviteMapper {

    public InviteInquiryMineResponse toInquiryMineResponse(Invite invite) {
        return new InviteInquiryMineResponse(invite.getCode(), invite.getRole(), invite.getCreatedAt());
    }
}
