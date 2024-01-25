package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardNoticePagingResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.mapper.BoardMapper;
import com.umc.networkingService.domain.board.repository.BoardRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffBoardServiceImpl implements StaffBoardService {
    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @Override
    public BoardNoticePagingResponse showNotices(Member member, HostType hostType, String keyword, Pageable pageable) {

        checkPermissionForNoticeBoard(member,hostType);

        //해당 운영진이 조회 가능한 최상위 hostType을 구함
        HostType permissionHostType = HostType.getPermmissionHostType(member.getRole());

        // findNoticesByHostType -> 해당 hostType만 조회 가능 (해당 멤버가 조회 가능한 hostType인지 확인 필요)
        // findAllNotices -> 해당 운영진이 조회 가능한 모든 hostType의 공지를 조회 가능
        if(hostType.equals(HostType.ALL))
            return boardMapper.toBoardNoticePagingResponse(boardRepository.findAllNotices(member, permissionHostType, keyword,pageable));
        else
            return boardMapper.toBoardNoticePagingResponse(boardRepository.findNoticesByHostType(member, hostType, keyword, pageable));

    }

    @Override
    @Transactional
    public BoardIdResponse toggleNoticePin(Member member, UUID boardId, boolean isPinned) {

        Board board = boardService.loadEntity(boardId);
        HostType hostType = board.getHostType();

        checkPermissionForNoticeBoard(member, hostType);

        board.setIsFixed(isPinned);

        return new BoardIdResponse(board.getId());
    }

    public void checkPermissionForNoticeBoard(Member member, HostType hostType) {

        //HostType priority와 Member Role priority를 비교하여 권한 CHECK
        if (member.getRole().getPriority() >= hostType.getPriority())
            throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);

    }
}
