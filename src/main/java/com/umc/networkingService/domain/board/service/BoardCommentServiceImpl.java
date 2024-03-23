package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCommentRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentResponse;
import com.umc.networkingService.domain.board.dto.response.MyBoardResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.mapper.BoardCommentMapper;
import com.umc.networkingService.domain.board.mapper.BoardMapper;
import com.umc.networkingService.domain.board.repository.BoardCommentRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.BoardCommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.*;
import static com.umc.networkingService.domain.board.dto.response.MyBoardResponse.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentServiceImpl implements BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardService boardService;
    private final BoardCommentMapper boardCommentMapper;
    private final BoardMapper boardMapper;
    private final MemberService memberService;

    @Override
    @Transactional
    public BoardCommentId addBoardComment(Member member, UUID commentId, BoardCommentRequest.BoardCommentAddRequest request) {
        Board board = boardService.loadEntity(request.getBoardId());

        BoardComment parentComment = Optional.ofNullable(commentId)
                .map(this::loadEntity).orElse(null);

        BoardComment comment = boardCommentRepository.save(
                boardCommentMapper.toEntity(member, board, parentComment, request.getContent()));

        board.increaseCommentCount();

        return new BoardCommentId(comment.getId());
    }



    @Override
    @Transactional
    public BoardCommentId updateBoardComment(Member loginMember, UUID commentId, BoardCommentRequest.BoardCommentUpdateRequest request) {
        Member member = memberService.loadEntity(loginMember.getId());
        BoardComment comment = loadEntity(commentId);

        //현재 로그인한 member와 writer가 같지 않으면 수정 권한 없음
        validateMember(comment, member);

        comment.update(request);

        return new BoardCommentId(comment.getId());
    }

    @Override
    @Transactional
    public BoardCommentId deleteBoardComment(Member loginMember, UUID commentId) {
        Member member = memberService.loadEntity(loginMember.getId());
        BoardComment comment = loadEntity(commentId);
        Board board = comment.getBoard();

        //현재 로그인한 member와 writer가 같지 않으면 삭제 권한 없음
        validateMember(comment, member);

        board.decreaseCommentCount();
        comment.delete();


        return new BoardCommentId(comment.getId());
    }

    @Override
    public BoardCommentPageInfos showBoardComments(Member loginMember, UUID boardId, Pageable pageable) {

        Member member = memberService.loadEntity(loginMember.getId());
        Board board = boardService.loadEntity(boardId);
        Page<BoardComment> comments = boardCommentRepository.findAllBoardComments(board, pageable);

        //isMine 여부를 포함
        List<BoardCommentPageElement> commentPageElements = comments.map(comment -> {
            boolean isMine = isMyComment(comment, member);
            return boardCommentMapper.toBoardCommentPageElement(comment, isMine);
        }).stream().toList();

        return boardCommentMapper.toBoardCommentPageInfos(comments, commentPageElements);
    }


    @Override
    public MyBoardPageInfos showBoardsByMemberCommentForApp(Member member, String keyword, Pageable pageable) {
        return boardMapper.toMyBoardPageInfos(boardCommentRepository.findBoardsByMemberCommentForApp(member, keyword, pageable));
    }

    @Override
    public MyBoardCommentPageInfos showBoardsByMemberCommentForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable) {
        return boardCommentMapper.toMyBoardCommentPageInfos(boardCommentRepository.findBoardsByMemberCommentForWeb(member, hostType, boardType, keyword, pageable));
    }

    private void validateMember(BoardComment comment, Member member) {
        if(!comment.getWriter().getId().equals(member.getId()))
            throw new RestApiException(BoardCommentErrorCode.NO_AUTHORIZATION_BOARD_COMMENT);

    }

    //본인 댓글인지 확인
    @Override
    public boolean isMyComment(BoardComment boardComment, Member member) {
        if (boardComment.getWriter().getId() == member.getId())
            return true;
        else
            return false;
    }


    @Override
    public BoardComment loadEntity(UUID commentId) {
        return boardCommentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(BoardCommentErrorCode.EMPTY_BOARD_COMMENT));
    }
}
