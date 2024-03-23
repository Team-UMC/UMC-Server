package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCommentRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCommentResponse;
import com.umc.networkingService.domain.board.dto.response.MyBoardResponse;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Board Comment 서비스의")
@SpringBootTest
public class BoardCommentServiceIntegrationTest extends BoardServiceTestConfig {


    @Test
    @DisplayName("댓글 작성 성공 테스트")
    @Transactional
    public void addBoardCommentTest() {
        //given
        BoardCommentRequest.BoardCommentAddRequest request = BoardCommentRequest.BoardCommentAddRequest.builder()
                .content("내용")
                .boardId(board.getId())
                .build();

        //when
        BoardCommentResponse.BoardCommentId response = boardCommentService.addBoardComment(inhaMember, null, request);
        Optional<BoardComment> optionalBoardComment = boardCommentRepository.findById(response.getCommentId());
        assertTrue(optionalBoardComment.isPresent());
        BoardComment comment = optionalBoardComment.get();

        //then
        assertEquals("내용", comment.getContent());
        assertEquals(board.getId(), comment.getBoard().getId());
        assertEquals(1, board.getCommentCount());
    }

    @Test
    @DisplayName("대댓글 작성 성공 테스트")
    @Transactional
    public void addReplyOnComment() {

        BoardCommentRequest.BoardCommentAddRequest request = BoardCommentRequest.BoardCommentAddRequest.builder()
                .boardId(board.getId())
                .content("대댓글")
                .build();

        BoardCommentResponse.BoardCommentId replyId = boardCommentService.addBoardComment(inhaMember, comment.getId(), request);

        BoardComment replyComment = boardCommentService.loadEntity(replyId.getCommentId());

        assertEquals(comment.getId(), replyComment.getParentComment().getId());
        assertEquals("대댓글", replyComment.getContent());
        assertEquals(board.getId(), replyComment.getBoard().getId());

    }

    @Test
    @DisplayName("댓글 수정 성공 테스트")
    @Transactional
    public void updateBoardCommentTest() {
        //given
        BoardCommentRequest.BoardCommentUpdateRequest request = BoardCommentRequest.BoardCommentUpdateRequest.builder()
                .content("수정")
                .build();

        //when
        boardCommentService.updateBoardComment(inhaMember, comment.getId(), request);

        //then
        assertEquals("수정", comment.getContent());
    }

    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    @Transactional
    public void deleteBoardCommentTest() {
        //given
        int commentCount = board.getCommentCount();
        //when
        boardCommentService.deleteBoardComment(inhaMember, comment.getId());

        //then
        assertNotNull(comment.getDeletedAt());
        assertEquals(commentCount - 1, comment.getBoard().getCommentCount());
    }

    @Test
    @DisplayName("댓글 목록 조회 테스트")
    @Transactional
    public void showBoardCommentsTest() {
        //given
        createBoardComments();
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Order.asc("created_at")));

        //when
        BoardCommentResponse.BoardCommentPageInfos response = boardCommentService.showBoardComments(inhaStaff, board.getId(), pageable);

        //then
        assertEquals(1, response.getPage());
        assertEquals(3, response.getTotalPages());
        assertEquals(21, response.getTotalElements());
        assertEquals(10, response.getBoardCommentPageElements().size());
        assertEquals("벡스/김준석", response.getBoardCommentPageElements().get(0).getWriter());
        assertFalse(response.getBoardCommentPageElements().get(0).getIsMine());
    }


    @Test
    @DisplayName("내가 댓글 쓴 글 목록 조회 테스트 -APP용")
    @Transactional
    public void showBoardsByMemberCommentTestForApp() {
        //given
        createBoardComments();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        MyBoardResponse.MyBoardPageInfos  response = boardCommentService.showBoardsByMemberCommentForApp(inhaMember, null, pageable);
        MyBoardResponse.MyBoardPageInfos response2 = boardCommentService.showBoardsByMemberCommentForApp(gachonMember, null, pageable);

        //then
        assertEquals(0, response.getPage());
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getMyBoardPageElements().size());
        assertEquals("제목",response.getMyBoardPageElements().get(0).getTitle());

        assertEquals(0, response.getPage());
        assertEquals(0, response2.getTotalPages());
        assertEquals(0, response2.getTotalElements());
        assertEquals(0, response2.getMyBoardPageElements().size());
    }

    @Test
    @DisplayName("내가 댓글 쓴 글 목록 조회 테스트 - WEB용")
    @Transactional
    public void showBoardsByMemberCommentTestForWeb() {
        //given
        createBoardComments();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        MyBoardResponse.MyBoardCommentPageInfos response = boardCommentService.showBoardsByMemberCommentForWeb(inhaStaff, HostType.CAMPUS, BoardType.FREE,null, pageable);

        //then
        assertEquals(0, response.getPage());
        assertEquals(2, response.getTotalPages());
        assertEquals(20, response.getTotalElements());
        assertEquals(10, response.getMyBoardCommentPageElement().size());
        assertEquals(HostType.CAMPUS, response.getMyBoardCommentPageElement().get(0).getHostType());
        assertEquals(BoardType.FREE, response.getMyBoardCommentPageElement().get(0).getBoardType());
    }
}
