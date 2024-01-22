package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.request.BoardCommentUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentIdResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.board.mapper.BoardCommentMapper;
import com.umc.networkingService.domain.board.repository.BoardCommentRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
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
    @Override
    @Transactional
    public BoardCommentIdResponse addBoardComment(Member member, BoardCommentAddRequest request) {
        Board board = boardService.loadEntity(request.getBoardId());

        BoardComment comment = boardCommentRepository.save(
                boardCommentMapper.toEntity(member, board, request));
        board.increaseCommentCount();

        return new BoardCommentIdResponse(comment.getId());
    }

    @Override
    @Transactional
    public BoardCommentIdResponse updateBoardComment(Member member, UUID commentId,
                                                     BoardCommentUpdateRequest request) {
        BoardComment comment = loadEntity(commentId);

        comment.update(request);

        return new BoardCommentIdResponse(comment.getId());
    }

    @Override
    @Transactional
    public BoardCommentIdResponse deleteBoardComment(Member member, UUID commentId) {

        BoardComment comment = loadEntity(commentId);
        Board board = comment.getBoard();

        board.decreaseCommentCount();
        comment.delete();


        return new BoardCommentIdResponse(comment.getId());
    }

    @Override
    public BoardComment loadEntity(UUID commentId) {
        return boardCommentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(ErrorCode.EMPTY_BOARD_COMMENT));
    }
}
