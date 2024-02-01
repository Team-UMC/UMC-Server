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


    
    /*
    HostType, BoardType에 따라 게시글 목록 조회
     */
    @Override
    public Page<Board> findAllBoards(Member member, HostType hostType, BoardType boardType, Pageable pageable) {

        List<Semester> memberSemesters = member.getSemesters();
        BooleanBuilder predicate = new BooleanBuilder()
                .and(eqBoardType(boardType));

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

    /*
    검색 keyword에 따라 게시글 목록 조회
     */
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

    /*
    게시글의 모든 댓글 목록 조회
     */
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


    /* 
    APP : 내가쓴 글 목록 조회
     */
    @Override
    public Page<Board> findBoardsByWriterForApp(Member member, String keyword, Pageable pageable) {
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


    /*
    WEB: HostType, BoardType에 따라 내가 쓴 글 목록 조회
     */
    @Override
    public Page<Board> findBoardsByWriterForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable) {

        BooleanBuilder predicate = addHostTypeAndBoardTypeCondition(member, hostType, boardType)
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


    /*
    APP : 내가 댓글 쓴 글 목록 조회
     */
    @Override
    public Page<Board> findBoardsByMemberCommentForApp(Member member, String keyword, Pageable pageable) {

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

    /*
    WEB: HostType, BoardType에 따라 내가 댓글 쓴 글, 내가 쓴 댓글 목록 조회
    */
    @Override
    public Page<BoardComment> findBoardsByMemberCommentForWeb(Member member, HostType hostType, BoardType boardType,
                                                              String keyword, Pageable pageable) {

        BooleanBuilder predicate = addHostTypeAndBoardTypeCondition(member, hostType, boardType)
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

    /* 
    APP : 내가 좋아요한 글 목록 조회
     */
    @Override
    public Page<Board> findBoardsByMemberHeartForApp(Member member, String keyword, Pageable pageable) {

        BooleanBuilder predicate = new BooleanBuilder()
                .and(boardHeart.member.eq(member))
                .and(boardHeart.isChecked.isTrue());

        addKeywordSearchCondition(predicate, keyword);

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

    /*
    WEB: HostType, BoardType에 따라 내가 좋아요한 글 목록 조회
    */
    @Override
    public Page<Board> findBoardsByMemberHeartForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable) {


        BooleanBuilder predicate = addHostTypeAndBoardTypeCondition(member, hostType, boardType)
                .and(boardHeart.member.eq(member))
                .and(boardHeart.isChecked.isTrue());

        addKeywordSearchCondition(predicate, keyword);

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


    /*
    HostType에 따른 공지글 조회 (운영진용)
     */
    @Override
    public Page<Board> findNoticesByHostType(Member member, HostType hostType, String keyword, Pageable pageable) {

        BooleanBuilder predicate = addHostTypeAndBoardTypeCondition(member, hostType, BoardType.NOTICE);
        addKeywordSearchCondition(predicate, keyword);

        List<Board> boards = query.selectFrom(board).where(predicate)
                .orderBy(board.isFixed.desc(), board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        return new PageImpl<>(boards, pageable, query.selectFrom(board).where(predicate).fetch().size());

    }

    /*
    permissionHostType에 따라 최상위 권한이하의 공지글 목록을 조회
    */
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



    //hostTYpe과 boardType에 따라 조건 추가
    private BooleanBuilder addHostTypeAndBoardTypeCondition(Member member, HostType hostType, BoardType boardType) {
        BooleanBuilder predicate = new BooleanBuilder().and(eqBoardType(boardType));

        switch (hostType) {
            case CAMPUS -> predicate.and(CampusPermission(member));
            case BRANCH -> predicate.and(BranchPermission(member));
            case CENTER -> predicate.and(CenterPermission());
        }

        return predicate;
    }

    //keyword가 비지 않았으면 keyword검색 조건 추가하는 함수
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
        return eqHostType(CAMPUS).and(eqUniversity(member));
    }
}

