package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCommentRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentResponse;
import com.umc.networkingService.domain.board.dto.response.BoardResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.umc.networkingService.domain.board.dto.response.BoardCommentResponse.*;

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

        return new BoardCommentResponse.BoardCommentId(comment.getId());
    }



    @Override
    @Transactional
    public BoardCommentId updateBoardComment(Member loginMember, UUID commentId, BoardCommentRequest.BoardCommentUpdateRequest request) {
        Member member = memberService.loadEntity(loginMember.getId());
        BoardComment comment = loadEntity(commentId);

        //현재 로그인한 member와 writer가 같지 않으면 수정 권한 없음
        validateMember(comment, member);

        comment.update(request);

        return new BoardCommentResponse.BoardCommentId(comment.getId());
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


        return new BoardCommentResponse.BoardCommentId(comment.getId());
    }

    @Override
    public BoardCommentPageInfos<BoardCommentPageElement> showBoardComments(Member loginMember, UUID boardId, Pageable pageable) {

        Member member = memberService.loadEntity(loginMember.getId());
        Board board = boardService.loadEntity(boardId);
        Page<BoardComment> comments = boardCommentRepository.findAllBoardComments(board, pageable);

        //isMine 여부를 포함
        List<BoardCommentPageElement> commentPageElements = comments.map(comment ->
                boardCommentMapper.toBoardCommentPageElement(comment,
                        boardMapper.toWriterInfo(comment.getWriter()),
                        isMyComment(comment, member))).stream().toList();

        return boardCommentMapper.toBoardCommentPageInfos(comments, commentPageElements);
    }


    @Override
    public BoardResponse.BoardPageInfos<BoardResponse.MyBoardPageElement> showBoardsByMemberCommentForApp(Member member, String keyword, Pageable pageable) {

        Page<Board> boards = boardCommentRepository.findBoardsByMemberCommentForApp(member, keyword, pageable);
        return boardMapper.toBoardPageInfos(boards, boards.map(boardMapper::toMyBoardPageElement).stream().toList());
    }

    @Override
    public BoardCommentPageInfos<MyBoardCommentPageElement> showBoardsByMemberCommentForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable) {
        Page<BoardComment> boardComments = boardCommentRepository.findBoardsByMemberCommentForWeb(member, hostType, boardType, keyword, pageable);

        return boardCommentMapper.toBoardCommentPageInfos(boardComments,
                boardComments.map(boardCommentMapper::toMyBoardCommentPageElement).stream().toList());
    }

    private void validateMember(BoardComment comment, Member member) {
        if(!comment.getWriter().getId().equals(member.getId()))
            throw new RestApiException(BoardCommentErrorCode.NO_AUTHORIZATION_BOARD_COMMENT);

    }

    //본인 댓글인지 확인
    @Override
    public boolean isMyComment(BoardComment boardComment, Member member) {
        return boardComment.getWriter().getId() == member.getId();
    }


    @Override
    public BoardComment loadEntity(UUID commentId) {
        return boardCommentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(BoardCommentErrorCode.EMPTY_BOARD_COMMENT));
    }
}
