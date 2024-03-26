package com.umc.networkingService.domain.board.repository;


import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> findPinnedNoticesByMember(Member member);

    List<Board> findPinnedNoticesByMemberAndHostType(Member member, HostType hostType);

    Page<Board> findAllBoards(Member member, HostType hostType, BoardType boardType, Pageable pageable);

    Page<Board> findKeywordBoards(Member member, String keyword, Pageable pageable);

    Page<BoardComment> findAllBoardComments(Board board, Pageable pageable);

    Page<Board> findBoardsByWriterForApp(Member member, String keyword, Pageable pageable);

    Page<Board> findBoardsByWriterForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);

    Page<Board> findBoardsByMemberCommentForApp(Member member, String keyword, Pageable pageable);

    Page<BoardComment> findBoardsByMemberCommentForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);

    Page<Board> findBoardsByMemberHeartForApp(Member member, String keyword, Pageable pageable);

    Page<Board> findBoardsByMemberHeartForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable);

    Page<Board> findNoticesByHostType(Member member, HostType hostType, String keyword, Pageable pageable);

    Page<Board> findAllNotices(Member member, HostType permissionHostType, String keyword, Pageable pageable);
}
