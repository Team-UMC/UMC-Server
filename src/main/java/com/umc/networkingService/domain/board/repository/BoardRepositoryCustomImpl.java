package com.umc.networkingService.domain.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.networkingService.domain.board.entity.*;
import com.umc.networkingService.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public Page<Board> findAllBoards(Member member, HostType hostType, BoardType boardType, Pageable pageable) {
        QBoard board = QBoard.board;

        BooleanBuilder predicate = new BooleanBuilder();

        //hostType에 따른 동적 쿼리
        switch (hostType) {
            case CAMPUS -> predicate.and(board.writer.university.eq(member.getUniversity())
                    .and(board.hostType.eq(HostType.CAMPUS))
                    .and(board.boardType.eq(boardType))
                    .and(board.deletedAt.isNull()));
            case BRANCH -> predicate.and(board.writer.branch.eq(member.getBranch())
                    .and(board.hostType.eq(HostType.BRANCH))
                    .and(board.boardType.eq(boardType))
                    .and(board.deletedAt.isNull()));
            case CENTER -> predicate.and(board.hostType.eq(HostType.CENTER)
                    .and(board.boardType.eq(boardType))
                    .and(board.deletedAt.isNull()));

        }

        List<Board> boards = query.selectFrom(board).where(predicate).fetch();
        return new PageImpl<>(boards, pageable, boards.size());
    }

    @Override
    public Page<Board> findKeywordBoards(Member member, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;

        List<Board> boards = query.selectFrom(board)
                .where(board.title.contains(keyword)
                        .or(board.content.contains(keyword))
                        .and(board.deletedAt.isNull()))
                .fetch();

        return new PageImpl<>(boards, pageable, boards.size());
    }

    @Override
    public Page<BoardComment> findAllBoardComments(Member member, Board board, Pageable pageable) {
        QBoardComment boardComment = QBoardComment.boardComment;

        List<BoardComment> boardComments = query.selectFrom(boardComment)
                .where(boardComment.board.eq(board)
                        .and(boardComment.deletedAt.isNull()))
                .fetch();
        return new PageImpl<>(boardComments, pageable, boardComments.size());
    }


}
