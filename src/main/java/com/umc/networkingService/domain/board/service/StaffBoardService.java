package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.response.BoardResponse.BoardId;
import com.umc.networkingService.domain.board.dto.response.BoardResponse.BoardPageInfos;
import com.umc.networkingService.domain.board.dto.response.BoardResponse.NoticePageElement;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StaffBoardService {
    BoardPageInfos<NoticePageElement> showNotices(Member member, HostType hostType, String keyword, Pageable pageable);

    BoardId toggleNoticePin(Member member, UUID boardId, boolean isFixed);
}
