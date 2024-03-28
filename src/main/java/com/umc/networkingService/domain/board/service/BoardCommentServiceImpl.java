package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCommentRequest;
import com.umc.networkingService.domain.board.dto.request.BoardCommentRequest.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentResponse;
import com.umc.networkingService.domain.board.dto.response.BoardResponse.BoardPageInfos;
import com.umc.networkingService.domain.board.dto.response.BoardResponse.MyBoardCommentPageElement;
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
import com.umc.networkingService.global.common.exception.code.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.*;
import static com.umc.networkingService.domain.board.dto.response.BoardResponse.MyBoardPageElement;

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
    public BoardCommentId addBoardComment(Member member, UUID commentId, BoardCommentAddRequest request) {
        Board board = boardService.loadEntity(request.getBoardId());

        BoardComment parentComment = Optional.ofNullable(commentId)
                .map(this::loadEntity).orElse(null);

        BoardComment comment = boardCommentRepository.save(
                boardCommentMapper.toEntity(member, board, parentComment, request.getContent()));

        board.increaseCommentCount();

        return new BoardCommentResponse.BoardCommentId(comment.getId());
    }


    @Override
    @Transactional
    public BoardCommentId updateBoardComment(Member loginMember, UUID commentId, BoardCommentRequest.BoardCommentUpdateRequest request) {
        Member member = memberService.loadEntity(loginMember.getId());
        BoardComment comment = loadEntity(commentId);

        //현재 로그인한 member와 writer가 같지 않으면 수정 권한 없음
        if (!boardService.checkWriter(comment.getWriter(), member))
            throw new RestApiException(BoardCommentErrorCode.NO_AUTHORIZATION_BOARD_COMMENT);

        comment.update(request);

        return new BoardCommentResponse.BoardCommentId(comment.getId());
    }

    @Override
    @Transactional
    public BoardCommentId deleteBoardComment(Member loginMember, UUID commentId) {
        Member member = memberService.loadEntity(loginMember.getId());
        BoardComment comment = loadEntity(commentId);
        Board board = comment.getBoard();
        Member writer = comment.getWriter();

        //현재 로그인한 member와 writer가 같지 않고, 로그인 한 멤버보다 상위 운영진이 아니라면 예외 반환
        if (!boardService.checkWriter(writer, member)) {
            if (!boardService.checkHighStaff(writer, member)) {
                throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);
            }
        }

        board.decreaseCommentCount();
        handleCommentDeletion(comment);

        return new BoardCommentResponse.BoardCommentId(comment.getId());
    }

    @Override
    public BoardCommentPageInfos<BoardCommentPageElement> showBoardComments(Member loginMember, UUID boardId, Pageable pageable) {

        Member member = memberService.loadEntity(loginMember.getId());
        Board board = boardService.loadEntity(boardId);

        boardService.checkReadPermission(member, board);

        Page<BoardComment> comments = boardCommentRepository.findAllBoardComments(board, pageable);

        //isMine 여부를 포함
        List<BoardCommentPageElement> commentPageElements = comments.map(comment ->
                boardCommentMapper.toBoardCommentPageElement(comment,
                        boardMapper.toDetailWriterInfo(comment.getWriter()),
                        boardService.checkWriter(comment.getWriter(), member))).stream().toList();

        return boardCommentMapper.toBoardCommentPageInfos(comments, commentPageElements);
    }


    @Override
    public BoardPageInfos<MyBoardPageElement> showBoardsByMemberCommentForApp(Member member, String keyword, Pageable pageable) {

        Page<Board> boards = boardCommentRepository.findBoardsByMemberCommentForApp(member, keyword, pageable);
        return boardMapper.toBoardPageInfos(boards, boards.map(boardMapper::toMyBoardPageElement).stream().toList());
    }

    @Override
    public BoardCommentPageInfos<MyBoardCommentPageElement> showBoardsByMemberCommentForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable) {
        Page<BoardComment> boardComments = boardCommentRepository.findBoardsByMemberCommentForWeb(member, hostType, boardType, keyword, pageable);

        return boardCommentMapper.toBoardCommentPageInfos(boardComments,
                boardComments.map(boardCommentMapper::toMyBoardCommentPageElement).stream().toList());
    }

    private void handleCommentDeletion(BoardComment comment) {
        if (boardCommentRepository.existsByParentComment(comment)) {
            comment.deleteComment();
        } else {
            comment.delete();
        }
    }

    @Override
    public BoardComment loadEntity(UUID commentId) {
        return boardCommentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(BoardCommentErrorCode.EMPTY_BOARD_COMMENT));
    }
}
