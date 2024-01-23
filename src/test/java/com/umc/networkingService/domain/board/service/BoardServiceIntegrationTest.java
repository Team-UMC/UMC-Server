package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.dto.request.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.request.BoardCommentUpdateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.*;
import com.umc.networkingService.domain.board.entity.*;
import com.umc.networkingService.domain.board.repository.BoardCommentRepository;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Board 서비스의 ")
@SpringBootTest
@Transactional
public class BoardServiceIntegrationTest  extends BoardServiceTestConfig{
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardCommentService boardCommentService;

    @Autowired
    BoardCommentRepository boardCommentRepository;
    @Test
    @DisplayName("게시글 작성 성공 테스트")
    public void createBoardTest() {
        //given
        BoardCreateRequest request = BoardCreateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType(BoardType.FREE)
                .hostType(HostType.CAMPUS)
                .build();

        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes()));
        files.add(new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes()));

        //when
        BoardIdResponse response = boardService.createBoard(member, request, files);
        Optional<Board> optionalBoard = boardRepository.findById(response.getBoardId());
        assertTrue(optionalBoard.isPresent());
        Board board = optionalBoard.get();
        List<BoardFile> boardFiles = boardFileRepository.findAllByBoard(board);

        //then
        assertEquals("제목", board.getTitle());
        assertEquals("내용", board.getContent());
        assertEquals("FREE", board.getBoardType().toString());
        assertEquals("CAMPUS", board.getHostType().toString());
        assertEquals(2, boardFiles.size());
    }

    @Test
    @DisplayName("공지 사항 게시판 게시글 작성 실패 테스트(권한 없음)")
    public void createBoardNotice() {
        //given
        BoardCreateRequest request = BoardCreateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType(BoardType.NOTICE)
                .hostType(HostType.CAMPUS)
                .build();

        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes()));
        files.add(new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes()));


        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            boardService.createBoard(member, request, files);
        });

        //then
        assertEquals(ErrorCode.FORBIDDEN_MEMBER, exception.getErrorCode());
    }

    @Test
    @DisplayName("OB 게시판 게시글 작성 실패 테스트(권한 없음)")
    public void createBoardOB() {
        //given
        BoardCreateRequest request = BoardCreateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType(BoardType.OB)
                .hostType(HostType.CAMPUS)
                .build();

        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes()));
        files.add(new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes()));


        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            boardService.createBoard(member, request, files);
        });

        //then
        assertEquals(ErrorCode.FORBIDDEN_MEMBER, exception.getErrorCode());
    }

    @Test
    @DisplayName("Workbook 게시판 게시글 작성 실패 테스트(권한 없음)")
    public void createBoardWorkbook() {
        //given
        BoardCreateRequest request = BoardCreateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType(BoardType.WORKBOOK)
                .hostType(HostType.CAMPUS)
                .build();

        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes()));
        files.add(new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes()));


        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            boardService.createBoard(member, request, files);
        });
        //then
        assertEquals(ErrorCode.FORBIDDEN_MEMBER, exception.getErrorCode());
    }

    @Test
    @DisplayName("게시글 수정 성공 테스트")
    public void updateBoardTest() {
        //given
        BoardUpdateRequest request = BoardUpdateRequest.builder()
                .title("수정제목")
                .content("수정내용")
                .boardType(BoardType.QUESTION)
                .hostType(HostType.CAMPUS)
                .build();

        UUID boardId = board.getId();
        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("file", "fileupdate.jpg", "image/jpeg", "file content".getBytes()));

        //when
        boardService.updateBoard(member, boardId, request, files);
        List<BoardFile> boardFiles = boardFileRepository.findAllByBoard(board);

        //then
        assertEquals("수정제목", board.getTitle());
        assertEquals("수정내용", board.getContent());
        assertEquals("QUESTION", board.getBoardType().toString());
        assertEquals("CAMPUS", board.getHostType().toString());
        assertEquals(1, boardFiles.size());
    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    public void deleteBoardTest() {
        //given
        UUID boardId = board.getId();

        //when
        boardService.deleteBoard(member, boardId);
        List<BoardFile> boardFiles = boardFileRepository.findAllByBoard(board);

        //then
        assertNotNull(board.getDeletedAt());
        boardFiles.forEach(boardImage -> assertNotNull(boardImage.getDeletedAt()));
    }

    @Test
    @DisplayName("특정 게시글 상세 조회 테스트")
    public void showBoardDetailTest() {
        //given
        createBoardFile();
        UUID boardId = board.getId();

        //when
        BoardDetailResponse boardDetailResponse = boardService.showBoardDetail(member, boardId);

        //then
        assertEquals("루시/김수민", boardDetailResponse.getWriter());
        assertEquals(1, boardDetailResponse.getHitCount());
        assertEquals(Semester.FIFTH, boardDetailResponse.getSemester());
        assertEquals(Part.SPRING, boardDetailResponse.getPart());
        assertEquals(0, boardDetailResponse.getHeartCount());
        assertEquals(2, boardDetailResponse.getBoardFiles().size());
    }

    @Test
    @DisplayName("특정 게시판의 게시글 목록 조회 테스트")
    public void showBoardsTest() {
        //given
        HostType hostType = HostType.CAMPUS;
        BoardType boardType = BoardType.FREE;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardPagingResponse boardPagingResponse = boardService.showBoards(member, hostType, boardType, pageable);

        //then
        assertNotNull(boardPagingResponse.getBoardPagePostResponses().get(0).getBoardId());
        assertFalse(boardPagingResponse.getBoardPagePostResponses().get(0).isFixed());
        assertEquals(0, boardPagingResponse.getPage());
        assertEquals(1, boardPagingResponse.getBoardPagePostResponses().size());
        assertEquals(1, boardPagingResponse.getTotalElements());
    }


    @Test
    @DisplayName("게시글 검색 테스트")
    public void searchBoardTest() {
        //given
        String keyword = "데모데이";
        String keyword2 = "2";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));
        createBoards();

        //when
        BoardPagingResponse boardPagingResponse = boardService.searchBoard(member, keyword, pageable);
        BoardPagingResponse boardPagingResponse2 = boardService.searchBoard(member, keyword2, pageable);

        //then
        assertEquals(0, boardPagingResponse.getPage());
        assertEquals(2, boardPagingResponse.getTotalPages());
        assertEquals(11, boardPagingResponse.getTotalElements());
        assertEquals(10, boardPagingResponse.getBoardPagePostResponses().size());
        assertEquals("데모데이가 곧이네요10", boardPagingResponse.getBoardPagePostResponses().get(0).getTitle());


        assertEquals(0, boardPagingResponse2.getPage());
        assertEquals(1, boardPagingResponse2.getTotalPages());
        assertEquals(1, boardPagingResponse2.getTotalElements());
        assertEquals(1, boardPagingResponse2.getBoardPagePostResponses().size());


    }

    @Test
    @DisplayName("특정 게시글 좋아요/취소 테스트")
    public void toggleBoardLikeTest() {

        //given
        UUID boardId = board.getId();

        //when
        BoardIdResponse like = boardService.toggleBoardLike(member, boardId);

        //then
        assertEquals(1, board.getHeartCount());
        //given
        BoardIdResponse cancel = boardService.toggleBoardLike(member, boardId);

        //then
        assertEquals(0, board.getHeartCount());
    }

    @Test
    @DisplayName("댓글 작성 성공 테스트")
    public void addBoardCommentTest() {
        //given
        BoardCommentAddRequest request = BoardCommentAddRequest.builder()
                .content("내용")
                .boardId(board.getId())
                .build();

        //when
        BoardCommentIdResponse response = boardCommentService.addBoardComment(member, request);
        Optional<BoardComment> optionalBoardComment = boardCommentRepository.findById(response.getBoardCommentId());
        assertTrue(optionalBoardComment.isPresent());
        BoardComment comment = optionalBoardComment.get();

        //then
        assertEquals("내용", comment.getContent());
        assertEquals(board.getId(), comment.getBoard().getId());
        assertEquals(1, board.getCommentCount());
    }

    @Test
    @DisplayName("댓글 수정 성공 테스트")
    public void updateBoardCommentTest() {
        //given
        BoardCommentUpdateRequest request = BoardCommentUpdateRequest.builder()
                .content("수정")
                .build();

        //when
        boardCommentService.updateBoardComment(member,comment.getId(), request);

        //then
        assertEquals("수정", comment.getContent());
    }

    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    public void deleteBoardCommentTest() {
        //given
        int commentCount = board.getCommentCount();
        //when
        boardCommentService.deleteBoardComment(member,comment.getId());

        //then
        assertNotNull( comment.getDeletedAt());
        assertEquals(commentCount-1, comment.getBoard().getCommentCount());
    }

    @Test
    @DisplayName("댓글 목록 조회 테스트")
    public void showBoardCommentsTest() {
        //given
        createBoardComments();
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardCommentPagingResponse response = boardCommentService.showBoardComments(member,board.getId(),pageable);

        //then
        assertEquals(1, response.getPage());
        assertEquals(2, response.getTotalPages());
        assertEquals(16, response.getTotalElements());
        assertEquals(6, response.getBoardPageCommentResponses().size());
        assertEquals("좋아요.9", response.getBoardPageCommentResponses().get(0).getContent());
        assertEquals("벡스/김준석", response.getBoardPageCommentResponses().get(0).getWriter());
    }


    @Test
    @DisplayName("내가쓴 글 목록 조회 테스트")
    public void showMemberBoardsTest() {
        //given
        createBoards();
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardPagingResponse response = boardService.showMemberBoards(member,"데모",pageable);

        //then
        assertEquals(1, response.getPage());
        assertEquals(2, response.getTotalPages());
        assertEquals(11, response.getTotalElements());
        assertEquals(1, response.getBoardPagePostResponses().size());
        assertEquals(HostType.CENTER, response.getBoardPagePostResponses().get(0).getHostType());
        assertEquals(BoardType.FREE, response.getBoardPagePostResponses().get(0).getBoardType());
        assertEquals("루시/김수민", response.getBoardPagePostResponses().get(0).getWriter());
        assertEquals("데모데이가 곧이네요0", response.getBoardPagePostResponses().get(0).getTitle());


    }
}


