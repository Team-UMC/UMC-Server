package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.repository.BoardCommentRepository;
import com.umc.networkingService.domain.board.repository.BoardFileRepository;
import com.umc.networkingService.domain.board.repository.BoardHeartRepository;
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
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BoardServiceTestConfig {
    @Autowired
    protected BoardService boardService;
    @Autowired
    protected BoardFileService boardFileService;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected BoardRepository boardRepository;
    @Autowired
    protected BoardFileRepository boardFileRepository;
    @Autowired
    protected BoardCommentRepository boardCommentRepository;
    @Autowired
    protected BoardHeartRepository boardHeartRepository;
    @Autowired
    protected UniversityRepository universityRepository;
    @Autowired
    protected BranchRepository branchRepository;
    @Autowired
    protected SemesterPartRepository semesterPartRepository;


    protected Member member;
    protected Member campusStaff;
    protected Member centerStaff;
    protected Board board;
    protected Board workbookBoard;
    protected University university;
    protected Branch branch;
    protected BoardComment comment;

    @BeforeEach
    public void setUp() {
        university = createUniversity();
        branch = createBranch();
        campusStaff = createCampusStaff();
        centerStaff = createCenterStaff();
        member = createMember();
        board = createBoard();
        comment = createBoardComment();
        workbookBoard = createWorkbookBoard();
    }

    protected Member createCampusStaff() {
        return memberRepository.save(Member.builder()
                .id(UUID.randomUUID())
                .clientId("123456")
                .socialType(SocialType.KAKAO)
                .university(university)
                .branch(branch)
                .role(Role.CAMPUS_STAFF)
                .name("김준석")
                .nickname("벡스")
                .semesterParts(createSemesterPart(campusStaff))
                .build());
    }

    protected Member createCenterStaff() {
        return memberRepository.save(Member.builder()
                .id(UUID.randomUUID())
                .clientId("122222")
                .socialType(SocialType.KAKAO)
                .university(university)
                .branch(branch)
                .role(Role.CENTER_STAFF)
                .name("이서우")
                .nickname("우디")
                .build());
    }

    protected Member createMember() {
        return memberRepository.save(Member.builder()
                .id(UUID.randomUUID())
                .clientId("11111")
                .socialType(SocialType.KAKAO)
                .university(university)
                .branch(branch)
                .role(Role.MEMBER)
                .name("김수민")
                .nickname("루시")
                .semesterParts(createSemesterPart2(member))
                .build());
    }

    protected List<SemesterPart> createSemesterPart(Member member) {
        List<SemesterPart> semesterParts = List.of(
                SemesterPart.builder().member(member).part(Part.SPRING).semester(Semester.FIFTH).build(),
                SemesterPart.builder().member(member).part(Part.ANDROID).semester(Semester.THIRD).build()
        );

        return semesterPartRepository.saveAll(semesterParts);
    }

    protected List<SemesterPart> createSemesterPart2(Member member) {
        List<SemesterPart> semesterParts = List.of(
                SemesterPart.builder().member(member).part(Part.SPRING).semester(Semester.FIFTH).build()
        );

        return semesterPartRepository.saveAll(semesterParts);
    }


    protected University createUniversity() {
        return universityRepository.save(
                University.builder()
                        .name("인하대학교")
                        .build()
        );
    }

    protected Branch createBranch() {
        return branchRepository.save(
                Branch.builder()
                        .name("GACI")
                        .description("가치 지부입니다.")
                        .semester(Semester.FIFTH)
                        .build()
        );
    }

    protected Board createBoard() {
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

    protected Board createWorkbookBoard() {
        return boardRepository.save(Board.builder()
                .writer(campusStaff)
                .title("워크북")
                .content("내용")
                .boardType(BoardType.WORKBOOK)
                .hostType(HostType.CAMPUS)
                .semesterPermission(List.of(Semester.THIRD))
                .hitCount(0)
                .heartCount(0)
                .commentCount(0)
                .build());
    }

    protected Board createCampusNoticeBoard() {
        return boardRepository.save(Board.builder()
                .writer(campusStaff)
                .title("공지")
                .content("내용")
                .boardType(BoardType.NOTICE)
                .hostType(HostType.CAMPUS)
                .semesterPermission(List.of(Semester.values()))
                .hitCount(0)
                .heartCount(0)
                .commentCount(0)
                .build());
    }

    protected Board createCenterNoticeBoard() {
        return boardRepository.save(Board.builder()
                .writer(centerStaff)
                .title("연합공지")
                .content("내용")
                .boardType(BoardType.NOTICE)
                .hostType(HostType.CENTER)
                .semesterPermission(List.of(Semester.values()))
                .hitCount(0)
                .heartCount(0)
                .commentCount(0)
                .build());
    }

    protected void createBoardFile() {
        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes()));
        files.add(new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes()));

        boardFileService.uploadBoardFiles(board, files);

    }

    protected void createBoards() {
        for (int i = 0; i < 11; i++) {
            boardRepository.save(Board.builder()
                    .writer(member)
                    .title("데모데이가 곧이네요" + i)
                    .content("신난다~~")
                    .boardType(BoardType.FREE)
                    .hostType(HostType.CENTER)
                    .semesterPermission(List.of(Semester.FIFTH))
                    .hitCount(0)
                    .heartCount(0)
                    .commentCount(0)
                    .build());
        }
    }

    protected BoardComment createBoardComment() {
        return boardCommentRepository.save(BoardComment.builder()
                .writer(member)
                .board(board)
                .content("신나!")
                .build());
    }

    protected void createBoardComments() {
        for (int i = 0; i < 15; i++) {

            boardCommentRepository.save(BoardComment.builder()
                    .writer(campusStaff)
                    .board(board)
                    .content("좋아요." + i)
                    .build());
        }
    }

    protected void setBoardHeart(Member member, Board board) {
        boardService.toggleBoardLike(member, board.getId());
    }


}
