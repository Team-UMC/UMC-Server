package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.response.BoardResponse;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StaffBoardService {
    BoardResponse.NoticePageInfos showNotices(Member member, HostType hostType, String keyword, Pageable pageable);

    BoardResponse.BoardId toggleNoticePin(Member member, UUID boardId, boolean isPinned);
}
