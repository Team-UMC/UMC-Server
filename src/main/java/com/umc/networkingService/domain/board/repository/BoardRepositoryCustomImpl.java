package com.umc.networkingService.domain.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.networkingService.domain.board.entity.*;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.umc.networkingService.domain.board.entity.HostType.*;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory query;
    QBoard board = QBoard.board;
    QBoardComment boardComment = QBoardComment.boardComment;
    QBoardHeart boardHeart = QBoardHeart.boardHeart;


    @Override
    public Page<Board> findAllBoards(Member member, HostType hostType, BoardType boardType, Pageable pageable) {

        List<Semester> memberSemesters = member.getSemesters();
        BooleanBuilder predicate = new BooleanBuilder()
                .and(eqBoardType(boardType));

        //hostType에 따른 동적 쿼리
        switch (hostType) {
            case CAMPUS -> predicate.and(CampusPermission(member))
                    .and(board.semesterPermission.any().in(memberSemesters));
            case BRANCH -> predicate.and(BranchPermission(member));
            case CENTER -> predicate.and(CenterPermission());
        }

        List<Board> boards = query.selectFrom(board).where(predicate)
                .orderBy(board.isFixed.desc(), board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boards, pageable, query.selectFrom(board).where(predicate).fetch().size());
    }

    @Override
    public Page<Board> findKeywordBoards(Member member, String keyword, Pageable pageable) {

        List<Semester> memberSemesters = member.getSemesters();

        BooleanBuilder predicate = new BooleanBuilder()
                .and(board.semesterPermission.any().in(memberSemesters)); //semester 권한 check

        addKeywordSearchCondition(predicate, keyword);

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


        List<BoardComment> boardComments = query.selectFrom(boardComment)
                .where(boardComment.board.eq(board))
                .orderBy(boardComment.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boardComments, pageable, query.selectFrom(boardComment)
                .where(boardComment.board.eq(board))
                .fetch().size());
    }


    @Override
    public Page<Board> findBoardsByWriter(Member member, String keyword, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder()
                .and(board.writer.eq(member));

        addKeywordSearchCondition(predicate, keyword);

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
    public Page<Board> findBoardsByMemberCommentsForApp(Member member, String keyword, Pageable pageable) {

        BooleanBuilder predicate = new BooleanBuilder()
                .and(boardComment.writer.eq(member));

        addKeywordSearchCondition(predicate, keyword);

        List<Board> boards = query.select(board)
                .from(boardComment)
                .join(boardComment.board, board)
                .where(predicate)
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boards, pageable, query.select(board)
                .from(boardComment)
                .join(boardComment.board, board)
                .where(predicate)
                .fetch().size());
    }

    @Override
    public Page<BoardComment> findBoardsByMemberCommentsForWeb(Member member, String keyword, Pageable pageable) {

        BooleanBuilder predicate = new BooleanBuilder()
                .and(boardComment.writer.eq(member));

        addKeywordSearchCondition(predicate, keyword);

        List<BoardComment> boardComments = query.selectFrom(boardComment)
                .join(boardComment.board, board)
                .where(predicate)
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boardComments, pageable, query.selectFrom(boardComment)
                .join(boardComment.board, board)
                .where(predicate)
                .fetch().size());
    }

    @Override
    public Page<Board> findBoardsByMemberHearts(Member member, String keyword, Pageable pageable) {

        BooleanBuilder predicate = new BooleanBuilder()
                .and(boardHeart.member.eq(member))
                .and(boardHeart.isChecked.isTrue())
                .and(board.deletedAt.isNull());

        //keyword가 비지 않았으면 keyword검색 조건 추가
        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate.and(board.title.contains(keyword).or(board.content.contains(keyword)));
        }

        List<Board> boards = query.select(board)
                .from(boardHeart)
                .join(boardHeart.board, board)
                .where(predicate)
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boards, pageable, query.select(board)
                .from(boardHeart)
                .join(boardHeart.board, board)
                .where(predicate)
                .fetch().size());

    }

    @Override
    public Page<Board> findNoticesByHostType(Member member, HostType hostType, String keyword, Pageable pageable) {

        BooleanBuilder predicate = new BooleanBuilder()
                .and(board.boardType.eq(BoardType.NOTICE));

        //hostType에 따른 동적 쿼리
        switch (hostType) {
            case CAMPUS -> predicate.and(CampusPermission(member));
            case BRANCH -> predicate.and(BranchPermission(member));
            case CENTER -> predicate.and(CenterPermission());
        }

        addKeywordSearchCondition(predicate, keyword);

        List<Board> boards = query.selectFrom(board).where(predicate)
                .orderBy(board.isFixed.desc(), board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        return new PageImpl<>(boards, pageable, query.selectFrom(board).where(predicate).fetch().size());

    }

    @Override
    public Page<Board> findAllNotices(Member member, HostType permissionHostType, String keyword, Pageable pageable) {

        BooleanBuilder predicate = new BooleanBuilder();

        switch (permissionHostType) {
            case CENTER:
                predicate.and(CenterPermission());
            case BRANCH:
                predicate.or(BranchPermission(member));
            case CAMPUS:
                predicate.or(CampusPermission(member));
        }

        addKeywordSearchCondition(predicate, keyword);

        List<Board> boards = query.selectFrom(board).where(eqBoardType(BoardType.NOTICE), predicate)
                .orderBy(board.isFixed.desc(), board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(boards, pageable, query.selectFrom(board).where(eqBoardType(BoardType.NOTICE), predicate).fetch().size());

    }

    //keyword가 비지 않았으면 keyword검색 조건 추가
    private void addKeywordSearchCondition(BooleanBuilder predicate, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            predicate.and(board.title.contains(keyword).or(board.content.contains(keyword)));
        }
    }

    private BooleanExpression eqUniversity(Member member) {
        if (member != null && member.getUniversity() != null)
            return board.writer.university.eq(member.getUniversity());
        return null;
    }

    private BooleanExpression eqBranch(Member member) {
        if (member != null && member.getBranch() != null)
            return board.writer.branch.eq(member.getBranch());
        return null;
    }

    private BooleanExpression eqHostType(HostType hostType) {
        return board.hostType.eq(hostType);
    }

    private BooleanExpression eqBoardType(BoardType boardType) {
        return board.boardType.eq(boardType);
    }

    private BooleanExpression CenterPermission() {
        return eqHostType(CENTER);
    }

    private BooleanExpression BranchPermission(Member member) {
        return eqHostType(BRANCH).and(eqBranch(member));
    }

    private BooleanExpression CampusPermission(Member member) {
        return eqHostType(CAMPUS).and(eqUniversity(member)); //semester 권한 check);
    }
}

