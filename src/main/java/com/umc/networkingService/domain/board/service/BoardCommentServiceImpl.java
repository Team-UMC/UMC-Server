package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.comment.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.request.comment.BoardCommentUpdateRequest;
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
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.BoardCommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentServiceImpl implements BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardService boardService;
    private final BoardCommentMapper boardCommentMapper;
    private final BoardMapper boardMapper;

    @Override
    @Transactional
    public BoardCommentResponse.BoardCommentId addBoardComment(Member member, BoardCommentAddRequest request) {
        Board board = boardService.loadEntity(request.getBoardId());

        BoardComment comment = boardCommentRepository.save(
                boardCommentMapper.toEntity(member, board, request));
        board.increaseCommentCount();

        return new BoardCommentResponse.BoardCommentId(comment.getId());
    }

    @Override
    @Transactional
    public BoardCommentResponse.BoardCommentId updateBoardComment(Member member, UUID commentId,
                                                     BoardCommentUpdateRequest request) {
        BoardComment comment = loadEntity(commentId);

        //현재 로그인한 member와 writer가 같지 않으면 수정 권한 없음
        validateMember(comment, member);

        comment.update(request);

        return new BoardCommentResponse.BoardCommentId(comment.getId());
    }

    @Override
    @Transactional
    public BoardCommentResponse.BoardCommentId deleteBoardComment(Member member, UUID commentId) {

        BoardComment comment = loadEntity(commentId);
        Board board = comment.getBoard();

        //현재 로그인한 member와 writer가 같지 않으면 삭제 권한 없음
        validateMember(comment, member);

        board.decreaseCommentCount();
        comment.delete();


        return new BoardCommentResponse.BoardCommentId(comment.getId());
    }

    @Override
    public BoardCommentResponse.BoardCommentPageInfos showBoardComments(Member member, UUID boardId, Pageable pageable) {
        Board board = boardService.loadEntity(boardId);
        return boardCommentMapper.toBoardCommentPageInfos(
                boardCommentRepository.findAllBoardComments(member, board, pageable));
    }


    @Override
    public MyBoardResponse.MyBoardPageInfos showBoardsByMemberCommentForApp(Member member, String keyword, Pageable pageable) {
        return boardMapper.toMyBoardPageInfos(boardCommentRepository.findBoardsByMemberCommentForApp(member, keyword, pageable));
    }

    @Override
    public MyBoardResponse.MyBoardCommentPageInfos showBoardsByMemberCommentForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable) {
        return boardCommentMapper.toMyBoardCommentPageInfos(boardCommentRepository.findBoardsByMemberCommentForWeb(member, hostType, boardType, keyword, pageable));
    }

    private void validateMember(BoardComment comment, Member member) {
        if(!comment.getWriter().getId().equals(member.getId()))
            throw new RestApiException(BoardCommentErrorCode.NO_AUTHORIZATION_BOARD_COMMENT);

    }

    @Override
    public BoardComment loadEntity(UUID commentId) {
        return boardCommentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(BoardCommentErrorCode.EMPTY_BOARD_COMMENT));
    }
}
