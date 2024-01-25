package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.dto.request.comment.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.request.comment.BoardCommentUpdateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.*;
import com.umc.networkingService.domain.board.dto.response.comment.BoardCommentIdResponse;
import com.umc.networkingService.domain.board.dto.response.comment.BoardCommentPagingResponse;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardCommentPagingWebResponse;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardPagingResponse;
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
public class BoardServiceIntegrationTest extends BoardServiceTestConfig {
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardCommentService boardCommentService;
    @Autowired
    private StaffBoardService staffBoardService;
    @Autowired
    BoardCommentRepository boardCommentRepository;

    @Test
    @DisplayName("게시글 작성 성공 테스트")
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @DisplayName("Workbook 게시판 게시글 조회 실패 테스트(권한 없음)")
    @Transactional
    public void showBoardWorkbook() {

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            boardService.showBoardDetail(member, workbookBoard.getId());
        });

        //then
        assertEquals(ErrorCode.FORBIDDEN_MEMBER, exception.getErrorCode());
    }


    @Test
    @DisplayName("게시글 수정 성공 테스트")
    @Transactional
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
    @Transactional
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
    @Transactional
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
        assertEquals(false, boardDetailResponse.isLiked());
        assertEquals(0, boardDetailResponse.getHeartCount());
        assertEquals(2, boardDetailResponse.getBoardFiles().size());

    }

    @Test
    @DisplayName("특정 게시판의 게시글 목록 조회 테스트")
    @Transactional
    public void showBoardsTest() {
        //given
        HostType hostType = HostType.CAMPUS;
        BoardType boardType = BoardType.FREE;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardPagingResponse boardPagingResponse = boardService.showBoards(member, hostType, boardType, pageable);

        //then
        assertNotNull(boardPagingResponse.getBoardPageResponses().get(0).getBoardId());
        assertFalse(boardPagingResponse.getBoardPageResponses().get(0).isFixed());
        assertEquals(0, boardPagingResponse.getPage());
        assertEquals(1, boardPagingResponse.getBoardPageResponses().size());
        assertEquals(1, boardPagingResponse.getTotalElements());
    }

    @Test
    @DisplayName("특정 게시판의 게시글 목록 조회 테스트 - 기수 권한 체크")
    @Transactional
    public void showBoardsSemesterPermissionsTest() {
        //given
        HostType hostType = HostType.CAMPUS;
        BoardType boardType = BoardType.WORKBOOK;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardPagingResponse boardPagingResponse = boardService.showBoards(member, hostType, boardType, pageable);

        //then
        assertEquals(0, boardPagingResponse.getBoardPageResponses().size());
        assertEquals(0, boardPagingResponse.getTotalElements());

        //when
        BoardPagingResponse boardPagingResponse2 = boardService.showBoards(campusStaff, hostType, boardType, pageable);

        //then
        assertEquals(1, boardPagingResponse2.getBoardPageResponses().size());
        assertEquals(1, boardPagingResponse2.getTotalElements());
    }


    @Test
    @DisplayName("게시글 검색 테스트")
    @Transactional
    public void searchBoardTest() {
        //given
        String keyword = "데모데이";
        String keyword2 = "2";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));
        createBoards();

        //when
        BoardSearchPagingResponse response = boardService.searchBoard(member, keyword, pageable);
        BoardSearchPagingResponse response2 = boardService.searchBoard(member, keyword2, pageable);

        //then
        assertEquals(0, response.getPage());
        assertEquals(2, response.getTotalPages());
        assertEquals(11, response.getTotalElements());
        assertEquals(10, response.getBoardSearchPageResponses().size());
        assertEquals(BoardType.FREE, response.getBoardSearchPageResponses().get(0).getBoardType());
        assertEquals(HostType.CENTER, response.getBoardSearchPageResponses().get(0).getHostType());


        assertEquals(0, response2.getPage());
        assertEquals(1, response2.getTotalPages());
        assertEquals(1, response2.getTotalElements());
        assertEquals(1, response2.getBoardSearchPageResponses().size());
    }

    @Test
    @DisplayName("특정 게시글 좋아요/취소 테스트")
    @Transactional
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
    @Transactional
    public void addBoardCommentTest() {
        //given
        BoardCommentAddRequest request = BoardCommentAddRequest.builder()
                .content("내용")
                .boardId(board.getId())
                .build();

        //when
        BoardCommentIdResponse response = boardCommentService.addBoardComment(member, request);
        Optional<BoardComment> optionalBoardComment = boardCommentRepository.findById(response.getCommentId());
        assertTrue(optionalBoardComment.isPresent());
        BoardComment comment = optionalBoardComment.get();

        //then
        assertEquals("내용", comment.getContent());
        assertEquals(board.getId(), comment.getBoard().getId());
        assertEquals(1, board.getCommentCount());
    }

    @Test
    @DisplayName("댓글 수정 성공 테스트")
    @Transactional
    public void updateBoardCommentTest() {
        //given
        BoardCommentUpdateRequest request = BoardCommentUpdateRequest.builder()
                .content("수정")
                .build();

        //when
        boardCommentService.updateBoardComment(member, comment.getId(), request);

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
        boardCommentService.deleteBoardComment(member, comment.getId());

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
        BoardCommentPagingResponse response = boardCommentService.showBoardComments(member, board.getId(), pageable);

        //then
        assertEquals(1, response.getPage());
        assertEquals(2, response.getTotalPages());
        assertEquals(16, response.getTotalElements());
        assertEquals(6, response.getBoardCommentPageResponses().size());
        assertEquals("벡스/김준석", response.getBoardCommentPageResponses().get(0).getWriter());
    }


    @Test
    @DisplayName("내가쓴 글 목록 조회 테스트")
    @Transactional
    public void showMemberBoardsTest() {
        //given
        createBoards();
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        MyBoardPagingResponse response = boardService.showBoardsByMember(member, "데모", pageable);

        //then
        assertEquals(1, response.getPage());
        assertEquals(2, response.getTotalPages());
        assertEquals(11, response.getTotalElements());
        assertEquals(1, response.getMyBoardPageResponses().size());
        assertEquals(HostType.CENTER, response.getMyBoardPageResponses().get(0).getHostType());
        assertEquals(BoardType.FREE, response.getMyBoardPageResponses().get(0).getBoardType());
    }

    @Test
    @DisplayName("내가 댓글 쓴 글 목록 조회 테스트 -APP용")
    @Transactional
    public void showBoardsByMemberCommentsForApp() {
        //given
        createBoardComments();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        MyBoardPagingResponse response = boardCommentService.showBoardsByMemberCommentsForApp(campusStaff, null, pageable);

        //then
        assertEquals(0, response.getPage());
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getMyBoardPageResponses().size());
    }

    @Test
    @DisplayName("내가 댓글 쓴 글 목록 조회 테스트 -WEB용")
    @Transactional
    public void showBoardsByMemberCommentsForWeb() {
        //given
        createBoardComments();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        MyBoardCommentPagingWebResponse response = boardCommentService.showBoardsByMemberCommentsForWeb(campusStaff, null, pageable);

        //then
        assertEquals(0, response.getPage());
        assertEquals(2, response.getTotalPages());
        assertEquals(15, response.getTotalElements());
        assertEquals(10, response.getMyBoardCommentPageWebResponses().size());
    }

    @Test
    @DisplayName("내가 좋아요한 글 목록 조회 테스트")
    @Transactional
    public void showMemberBoardHeartsTest() {
        //given
        setBoardHeart(campusStaff, board);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        MyBoardPagingResponse response = boardService.showBoardsByMemberHearts(campusStaff, null, pageable);

        //then
        assertEquals(0, response.getPage());
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getMyBoardPageResponses().size());
        assertEquals(1, response.getMyBoardPageResponses().get(0).getHeartCount());

    }


    @Test
    @DisplayName("운영진용 교내 공지사항 조회 테스트")
    @Transactional
    public void showStaffNoticesTest() {
        //given
        Board notice = createNoticeBoard();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardPagingResponse boardPagingResponse = staffBoardService.showAllCampusNotices(member, null, pageable);

        //then
        assertEquals(1, boardPagingResponse.getBoardPageResponses().size());
        assertEquals(1, boardPagingResponse.getTotalElements());
        assertEquals("공지", boardPagingResponse.getBoardPageResponses().get(0).getTitle());

    }


    @Test
    @DisplayName("운영진용 교내 공지사항 핀설정 테스트")
    @Transactional
    public void showStaffNoticePinTest() {
        //given
        Board notice = createNoticeBoard();
        assertEquals(false, notice.isFixed());

        //when
        staffBoardService.toggleNoticePin(member, notice.getId(), true);

        //then
        assertEquals(true, notice.isFixed());
    }

}


