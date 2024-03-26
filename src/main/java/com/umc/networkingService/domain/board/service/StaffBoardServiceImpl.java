package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.response.BoardResponse.BoardPageInfos;
import com.umc.networkingService.domain.board.dto.response.BoardResponse.NoticePageElement;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.mapper.BoardMapper;
import com.umc.networkingService.domain.board.repository.BoardRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.umc.networkingService.domain.board.dto.response.BoardResponse.BoardId;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffBoardServiceImpl implements StaffBoardService {
    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @Override
    public BoardPageInfos<NoticePageElement> showNotices(Member member, HostType hostType, String keyword, Pageable pageable) {
        Page<Board> notices;
        if(hostType == null) {
            //해당 운영진이 조회 가능한 최상위 hostType을 구함
            HostType permissionHostType = HostType.getPermmissionHostType(member.getRole());
            // findAllNotices -> 해당 운영진이 조회 가능한 모든 hostType의 공지를 조회 가능
            notices =  boardRepository.findAllNotices(member, permissionHostType, keyword, pageable);
        }
        else {
            // findNoticesByHostType -> 해당 hostType만 조회 가능 (해당 멤버가 조회 가능한 hostType인지 확인 필요)
            checkPermissionForNoticeBoard(member,hostType);
            notices = boardRepository.findNoticesByHostType(member, hostType, keyword, pageable);
        }

        return boardMapper.toBoardPageInfos(notices, notices.map(boardMapper::toNoticePageElement).stream().toList());
    }

    @Override
    @Transactional
    public BoardId toggleNoticePin(Member member, UUID boardId, boolean isFixed) {

        Board board = boardService.loadEntity(boardId);
        HostType hostType = board.getHostType();

        checkPermissionForNoticeBoard(member, hostType);

        board.setIsFixed(isFixed);

        return new BoardId(board.getId());
    }

    public void checkPermissionForNoticeBoard(Member member, HostType hostType) {

        //HostType priority와 Member Role priority를 비교하여 권한 CHECK
        if (member.getRole().getPriority() >= hostType.getPriority())
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);

    }
}
