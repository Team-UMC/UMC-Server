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

        List<Board> boards = query.selectFrom(board).where(predicate)
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boards, pageable, query.selectFrom(board).where(predicate).fetch().size());
    }

    @Override
    public Page<Board> findKeywordBoards(Member member, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;

        BooleanBuilder predicate = new BooleanBuilder()
                .and(board.title.contains(keyword))
                .or(board.content.contains(keyword))
                .and(board.deletedAt.isNull());

        List<Board> boards = query.selectFrom(board)
                .where(predicate)
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boards, pageable, query.selectFrom(board).where(predicate).fetch().size());
    }

    @Override
    public Page<BoardComment> findAllBoardComments(Member member, Board board, Pageable pageable) {
        QBoardComment boardComment = QBoardComment.boardComment;

        BooleanBuilder predicate = new BooleanBuilder()
                .and(boardComment.board.eq(board))
                .and(boardComment.deletedAt.isNull());

        List<BoardComment> boardComments = query.selectFrom(boardComment)
                .where(predicate)
                .orderBy(boardComment.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boardComments, pageable, query.selectFrom(boardComment)
                .where(predicate)
                .fetch().size());
    }


    @Override
    public Page<Board> findBoardsByWriter(Member member, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;

        BooleanBuilder predicate = new BooleanBuilder()
                .and(board.deletedAt.isNull())
                .and(board.writer.eq(member));

        //keyword가 비지 않았으면 keyword검색 조건 추가
        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate.and(board.title.contains(keyword).or(board.content.contains(keyword)));
        }

        List<Board> boards = query.selectFrom(board)
                .where(predicate)
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boards, pageable, query.selectFrom(board)
                .where(predicate)
                .fetch().size());

    }

    @Override
    public Page<BoardComment> findBoardCommentsByWriter(Member member, String keyword, Pageable pageable) {
        QBoardComment comment = QBoardComment.boardComment;

        BooleanBuilder predicate = new BooleanBuilder()
                .and(comment.deletedAt.isNull())
                .and(comment.writer.eq(member));

        //keyword가 비지 않았으면 keyword검색 조건 추가
        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate.and(comment.content.contains(keyword));
        }

        List<BoardComment> comments = query.selectFrom(comment)
                .where(predicate)
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(comments, pageable, query.selectFrom(comment)
                .where(predicate)
                .fetch().size());
    }

    @Override
    public Page<Board> findBoardHeartsByWriter(Member member, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QBoardHeart boardHeart = QBoardHeart.boardHeart;


        BooleanBuilder predicate = new BooleanBuilder()
                .and(boardHeart.member.eq(member))
                        .and(boardHeart.isChecked.isTrue())
                        .and(board.deletedAt.isNull());

        //keyword가 비지 않았으면 keyword검색 조건 추가
        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate.and(board.title.contains(keyword).or(board.content.contains(keyword)));
        }

        List<Board> boards = query.selectFrom(board)
                .select(board)
                .from(boardHeart)
                .join(boardHeart.board, board)
                .where(predicate)
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boards, pageable, query.selectFrom(board)
                .select(board)
                .from(boardHeart)
                .join(boardHeart.board, board)
                .where(predicate)
                .fetch().size());

    }


}
