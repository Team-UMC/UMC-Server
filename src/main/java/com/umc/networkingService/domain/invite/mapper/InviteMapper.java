package com.umc.networkingService.domain.invite.mapper;

import com.umc.networkingService.domain.invite.dto.response.InviteInquiryMineResponse;
import com.umc.networkingService.domain.invite.entity.Invite;
import org.springframework.stereotype.Component;

@Component
public class InviteMapper {

    public InviteInquiryMineResponse.InviteInfo toInquiryMineResponse(Invite invite) {
        return new InviteInquiryMineResponse.InviteInfo(invite.getCode(), invite.getRole(), invite.getCreatedAt());
    }
}
