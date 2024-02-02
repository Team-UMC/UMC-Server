package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.notice.BoardNoticePagingResponse;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StaffBoardService {
    BoardNoticePagingResponse showNotices(Member member, HostType hostType, String keyword, Pageable pageable);

    BoardIdResponse toggleNoticePin(Member member, UUID boardId, boolean isPinned);
}
