package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.dto.request.BoardRequest;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.BoardErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static com.umc.networkingService.domain.board.dto.response.BoardResponse.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Board 서비스의 ")
@SpringBootTest
public class BoardServiceIntegrationTest extends BoardServiceTestConfig {


    @Test
    @DisplayName("게시글 작성 성공 테스트")
    @Transactional
    public void createBoardTest() {
        //given
        BoardRequest.BoardCreateRequest request = BoardRequest.BoardCreateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType("FREE")
                .hostType("CAMPUS")
                .build();


        //file Test OFF
        List<MultipartFile> files = new ArrayList<>();
        //files.add(new MockMultipartFile("file", "filename1.pdf", "application/pdf", "file content".getBytes()));
        //files.add(new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes()));

        //when
        BoardId response = boardService.createBoard(inhaMember, request, files);
        Optional<Board> optionalBoard = boardRepository.findById(response.getBoardId());
        assertTrue(optionalBoard.isPresent());
        Board board = optionalBoard.get();
        //List<BoardFile> boardFiles = boardFileRepository.findAllByBoard(board);

        //then
        assertEquals("제목", board.getTitle());
        assertEquals("내용", board.getContent());
        assertEquals("FREE", board.getBoardType().toString());
        assertEquals("CAMPUS", board.getHostType().toString());
        //assertEquals(2, boardFiles.size());
    }

    @Test
    @DisplayName("공지 사항 게시판 게시글 작성 실패 테스트(권한 없음)")
    @Transactional
    public void createBoardNotice() {
        //given
        BoardRequest.BoardCreateRequest request = BoardRequest.BoardCreateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType("NOTICE")
                .hostType("CAMPUS")
                .build();

        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes()));
        files.add(new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes()));


        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            boardService.createBoard(inhaMember, request, files);
        });

        //then
        assertEquals(BoardErrorCode.NO_AUTHORIZATION_BOARD.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    @DisplayName("OB 게시판 게시글 작성 실패 테스트(권한 없음)")
    @Transactional
    public void createBoardOB() {
        //given
        BoardRequest.BoardCreateRequest request = BoardRequest.BoardCreateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType("OB")
                .hostType("CAMPUS")
                .build();

        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes()));
        files.add(new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes()));


        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            boardService.createBoard(inhaMember, request, files);
        });

        //then
        assertEquals(BoardErrorCode.NO_AUTHORIZATION_BOARD.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    @DisplayName("Workbook 게시판 게시글 작성 실패 테스트(권한 없음)")
    @Transactional
    public void createBoardWorkbook() {
        //given
        BoardRequest.BoardCreateRequest request = BoardRequest.BoardCreateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType("WORKBOOK")
                .hostType("CAMPUS")
                .build();

        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes()));
        files.add(new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes()));


        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            boardService.createBoard(inhaMember, request, files);
        });
        //then
        assertEquals(BoardErrorCode.NO_AUTHORIZATION_BOARD.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    @DisplayName("Workbook 게시판 게시글 조회 실패 테스트(권한 없음)")
    @Transactional
    public void showBoardWorkbook() {

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            boardService.showBoardDetail(inhaMember, workbookBoard.getId());
        });

        //then
        assertEquals(BoardErrorCode.NO_AUTHORIZATION_BOARD.getCode(), exception.getErrorCode().getCode());
    }


    @Test
    @DisplayName("게시글 수정 성공 테스트")
    @Transactional
    public void updateBoardTest() {
        //given
        BoardRequest.BoardUpdateRequest request = BoardRequest.BoardUpdateRequest.builder()
                .title("수정제목")
                .content("수정내용")
                .boardType("QUESTION")
                .hostType("CAMPUS")
                .build();

        UUID boardId = board.getId();

        //File Test OFF
        List<MultipartFile> files = new ArrayList<>();
        //files.add(new MockMultipartFile("file", "fileupdate.jpg", "image/jpeg", "file content".getBytes()));

        //when
        boardService.updateBoard(inhaMember, boardId, request, files);
        //List<BoardFile> boardFiles = boardFileRepository.findAllByBoard(board);

        //then
        assertEquals("수정제목", board.getTitle());
        assertEquals("수정내용", board.getContent());
        assertEquals("QUESTION", board.getBoardType().toString());
        assertEquals("CAMPUS", board.getHostType().toString());
        //assertEquals(1, boardFiles.size());
    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    @Transactional
    public void deleteBoardTest() {
        //given
        UUID boardId = board.getId();

        //when
        boardService.deleteBoard(inhaMember, boardId);

        //file Test OFF
        //List<BoardFile> boardFiles = boardFileRepository.findAllByBoard(board);

        //then
        assertNotNull(board.getDeletedAt());
        //boardFiles.forEach(boardImage -> assertNotNull(boardImage.getDeletedAt()));
    }

    @Test
    @DisplayName("특정 게시글 상세 조회 테스트")
    @Transactional
    public void showBoardDetailTest() {
        //given
        UUID boardId = board.getId();

        //when
        BoardDetail boardDetail = boardService.showBoardDetail(inhaMember, boardId);

        //then
        assertEquals("루시/김수민", boardDetail.getWriterInfo().getWriter());
        assertEquals(1, boardDetail.getHitCount());
        assertEquals(Semester.FIFTH, boardDetail.getWriterInfo().getSemester());
        assertEquals(Part.SPRING, boardDetail.getWriterInfo().getPart());
        assertEquals(false, boardDetail.getIsLiked());
        assertEquals(0, boardDetail.getHeartCount());
        assertTrue(boardDetail.getIsMine());
    }

    @Test
    @DisplayName("특정 게시글 상세 조회 실패 테스트")
    @Transactional
    public void showBoardDetailFailTest() {
        //given
        UUID boardId = board.getId();

        //when
        RestApiException exception = assertThrows(RestApiException.class, () -> {
            boardService.showBoardDetail(gachonMember, boardId);
        });
        //then
        assertEquals(BoardErrorCode.NO_AUTHORIZATION_BOARD.getCode(), exception.getErrorCode().getCode());
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
        BoardPageInfos<BoardPageElement> boardPageInfos = boardService.showBoards(inhaMember, hostType, boardType, pageable);

        //then
        assertNotNull(boardPageInfos.getBoardPageElements().get(0).getBoardId());
        assertEquals(0, boardPageInfos.getPage());
        assertEquals(1, boardPageInfos.getBoardPageElements().size());
        assertEquals(1, boardPageInfos.getTotalElements());

        //when
        BoardPageInfos<BoardPageElement>  boardPageInfos2 = boardService.showBoards(gachonMember, hostType, boardType, pageable);
        assertEquals(0, boardPageInfos2.getBoardPageElements().size());
        assertEquals(0, boardPageInfos2.getTotalElements());

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
        BoardPageInfos<BoardPageElement>  boardPageInfos = boardService.showBoards(inhaMember, hostType, boardType, pageable);

        //then
        assertEquals(0, boardPageInfos.getBoardPageElements().size());
        assertEquals(0, boardPageInfos.getTotalElements());

        //when
        BoardPageInfos<BoardPageElement>  boardPageInfos2 = boardService.showBoards(inhaStaff, hostType, boardType, pageable);

        //then
        assertEquals(1, boardPageInfos2.getBoardPageElements().size());
        assertEquals(1, boardPageInfos2.getTotalElements());
    }


    @Test
    @DisplayName("게시글 검색 테스트")
    @Transactional
    public void searchBoardTest() {
        //given
        String keyword = "데모데이";
        String keyword2 = "12";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));
        createBoards();

        //when
        BoardPageInfos<BoardSearchPageElement> response = boardService.searchBoard(inhaMember, keyword, pageable);
        BoardPageInfos<BoardSearchPageElement> response2 = boardService.searchBoard(inhaMember, keyword2, pageable);

        //then
        assertEquals(0, response.getPage());
        assertEquals(2, response.getTotalPages());
        assertEquals(20, response.getTotalElements());
        assertEquals(10, response.getBoardPageElements().size());
        assertEquals(BoardType.FREE, response.getBoardPageElements().get(0).getBoardType());
        assertEquals(HostType.CENTER, response.getBoardPageElements().get(0).getHostType());


        assertEquals(0, response2.getPage());
        assertEquals(1, response2.getTotalPages());
        assertEquals(1, response2.getTotalElements());
        assertEquals(1, response2.getBoardPageElements().size());
    }

    @Test
    @DisplayName("특정 게시글 좋아요/취소 테스트")
    @Transactional
    public void toggleBoardLikeTest() {

        //given
        UUID boardId = board.getId();

        //when
        BoardId like = boardService.toggleBoardLike(inhaMember, boardId);

        //then
        assertEquals(1, board.getHeartCount());
        //given
        BoardId cancel = boardService.toggleBoardLike(inhaMember, boardId);

        //then
        assertEquals(0, board.getHeartCount());
    }



    @Test
    @DisplayName("내가쓴 글 목록 조회 테스트 - APP용")
    @Transactional
    public void showBoardsByWriterTestForApp() {
        //given
        createBoards();
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardPageInfos<MyBoardPageElement> response = boardService.showBoardsByMember(inhaMember, null,null,"데모", pageable);

        //then
        assertEquals(1, response.getPage());
        assertEquals(2, response.getTotalPages());
        assertEquals(20, response.getTotalElements());
        assertEquals(10, response.getBoardPageElements().size());
        assertEquals(HostType.CENTER, response.getBoardPageElements().get(0).getHostType());
        assertEquals(BoardType.FREE, response.getBoardPageElements().get(0).getBoardType());
    }

    @Test
    @DisplayName("내가쓴 글 목록 조회 테스트 - WEB용")
    @Transactional
    public void showBoardsByWriterTestForWeb() {
        //given
        createBoards();
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardPageInfos<MyBoardPageElement> response = boardService.showBoardsByMember(inhaMember, HostType.CENTER,BoardType.FREE,"데모", pageable);

        //then
        assertEquals(1, response.getPage());
        assertEquals(2, response.getTotalPages());
        assertEquals(20, response.getTotalElements());
        assertEquals(10, response.getBoardPageElements().size());
        assertEquals(HostType.CENTER, response.getBoardPageElements().get(0).getHostType());
        assertEquals(BoardType.FREE, response.getBoardPageElements().get(0).getBoardType());
    }



    @Test
    @DisplayName("내가 좋아요한 글 목록 조회 테스트 - APP용")
    @Transactional
    public void showMemberBoardHeartTestForApp() {
        //given
        setBoardHeart(inhaStaff, board);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardPageInfos<MyBoardPageElement> response = boardService.showBoardsByMemberHeart(inhaStaff,null,null, null, pageable);

        //then
        assertEquals(0, response.getPage());
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getBoardPageElements().size());
        assertEquals(1, response.getBoardPageElements().get(0).getHeartCount());

    }

    @Test
    @DisplayName("내가 좋아요한 글 목록 조회 테스트 - WEB용")
    @Transactional
    public void showMemberBoardHeartTestForWeb() {
        //given
        setBoardHeart(inhaStaff, board);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardPageInfos<MyBoardPageElement> response = boardService.showBoardsByMemberHeart(inhaMember,HostType.CAMPUS,BoardType.WORKBOOK, null, pageable);

        //then
        assertEquals(0, response.getPage());
        assertEquals(0, response.getTotalPages());
        assertEquals(0, response.getTotalElements());
        assertEquals(0, response.getBoardPageElements().size());
    }



    @Test
    @DisplayName("운영진용 교내 공지사항 조회 테스트")
    @Transactional
    public void showStaffNoticesTest() {
        //given
        Board centerNotice = createCenterNoticeBoard();
        Board campusNotice = createCampusNoticeBoard();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));

        //when
        BoardPageInfos<NoticePageElement> noticePageInfos = staffBoardService.showNotices(centerStaff, null, null , pageable);

        //then
        assertEquals(1, noticePageInfos.getBoardPageElements().size());
        assertEquals(1, noticePageInfos.getTotalElements());
        assertEquals("연합공지", noticePageInfos.getBoardPageElements().get(0).getTitle());

    }


    @Test
    @DisplayName("운영진용 교내 공지사항 핀설정 테스트")
    @Transactional
    public void showStaffNoticePinTest() {
        //given
        Board notice = createCampusNoticeBoard();
        assertEquals(false, notice.isFixed());

        //when
        staffBoardService.toggleNoticePin(inhaStaff, notice.getId(), true);

        //then
        assertTrue(notice.isFixed());
    }

}


