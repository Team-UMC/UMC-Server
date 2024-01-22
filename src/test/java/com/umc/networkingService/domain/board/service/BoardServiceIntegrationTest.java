package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardDetailResponse;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardFile;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.repository.BoardFileRepository;
import com.umc.networkingService.domain.board.repository.BoardRepository;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.repository.BranchRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.repository.SemesterPartRepository;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.repository.UniversityRepository;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
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
public class BoardServiceIntegrationTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardFileRepository boardFileRepository;
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private SemesterPartRepository semesterPartRepository;


    private Member member;
    private Member member2;
    private Board board;
    private University university;
    private Branch branch;

    @BeforeEach
    public void setUp() {
        university = createUniversity();
        branch = createBranch();
        member = createMember();
        member2 = createMember2();
        board = createBoard();
    }

    private Member createMember() {
        return memberRepository.save(Member.builder()
                .id(UUID.randomUUID())
                .clientId("123456")
                .socialType(SocialType.KAKAO)
                .university(university)
                .branch(branch)
                .role(Role.MEMBER)
                .name("김준석")
                .nickname("벡스")
                .semesterParts(createSemesterPart(member))
                .build());
    }

    protected List<SemesterPart> createSemesterPart(Member member) {
        List<SemesterPart> semesterParts = List.of(
                SemesterPart.builder().member(member).part(Part.ANDROID).semester(Semester.FIFTH).build(),
                SemesterPart.builder().member(member).part(Part.SPRING).semester(Semester.THIRD).build()
        );

        return semesterPartRepository.saveAll(semesterParts);
    }


    private Member createMember2() {
        return memberRepository.save(Member.builder()
                .id(UUID.randomUUID())
                .clientId("11111")
                .socialType(SocialType.KAKAO)
                .university(university)
                .branch(branch)
                .role(Role.MEMBER)
                .name("김수민")
                .nickname("루시")
                .semesterParts(createSemesterPart(member2))
                .build());
    }


    private University createUniversity() {
        return universityRepository.save(
                University.builder()
                        .name("인하대학교")
                        .build()
        );
    }

    private Branch createBranch() {
        return branchRepository.save(
                Branch.builder()
                        .name("GACI")
                        .description("가치 지부입니다.")
                        .semester(Semester.FIFTH)
                        .build()
        );
    }

    private Board createBoard() {
        return boardRepository.save(Board.builder()
                .writer(member)
                .title("제목")
                .content("내용")
                .boardType(BoardType.FREE)
                .hostType(HostType.CAMPUS)
                .semesterPermission(List.of(Semester.FIFTH))
                .hitCount(0)
                .heartCount(0)
                .commentCount(0)
                .build());
    }

    private void createBoardFile() {
        boardFileRepository.save(BoardFile.builder()
                .board(board)
                .url("dfsefe.img")
                .build());
    }

    private void createBoards() {
        for (int i = 0; i < 11; i++) {
            boardRepository.save(Board.builder()
                    .writer(member)
                    .title("데모데이가 곧이에용")
                    .content("신난다~~")
                    .boardType(BoardType.FREE)
                    .hostType(HostType.CAMPUS)
                    .semesterPermission(List.of(Semester.FIFTH))
                    .hitCount(0)
                    .heartCount(1)
                    .commentCount(1)
                    .build());
        }
    }

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
        assertEquals("벡스/김준석", boardDetailResponse.getWriter());
        assertEquals(1, boardDetailResponse.getHitCount());
        assertEquals(Semester.FIFTH, boardDetailResponse.getSemester());
        assertEquals(Part.ANDROID, boardDetailResponse.getPart());
        assertEquals(0, boardDetailResponse.getHeartCount());
        assertEquals(1, boardDetailResponse.getBoardFiles().size());
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
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created_at")));
        createBoards();

        //when
        BoardPagingResponse boardPagingResponse = boardService.searchBoard(member, keyword, pageable);

        //then
        assertEquals(0, boardPagingResponse.getPage());
        assertEquals(2, boardPagingResponse.getTotalPages());
        assertEquals(11, boardPagingResponse.getTotalElements());
        assertEquals(11, boardPagingResponse.getBoardPagePostResponses().size());
        assertEquals("데모데이가 곧이에용", boardPagingResponse.getBoardPagePostResponses().get(1).getTitle());
    }

    @Test
    @DisplayName("특정 게시글 추천/취소 테스트")
    public void toggleBoardLikeTest() {

        //given
        UUID boardId = board.getId();

        //when
        BoardIdResponse like = boardService.toggleBoardLike(member2, boardId);

        //then
        assertEquals(1, board.getHeartCount());
    /*  assertEquals(1, board.getHearts().size());
        assertEquals("루시", board.getHearts().get(0).getMember().getNickname());
        assertEquals(true, board.getHearts().get(0).isChecked());
    */
        //given
        BoardIdResponse cancel = boardService.toggleBoardLike(member2, boardId);

        //then
        assertEquals(0, board.getHeartCount());
    /*  assertEquals(1, board.getHearts().size());
        assertEquals("루시", board.getHearts().get(0).getMember().getNickname());
        assertEquals(false, board.getHearts().get(0).isChecked());
    */
    }
}
