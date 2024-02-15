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
    @Autowired
    protected BoardCommentService boardCommentService;
    @Autowired
    protected StaffBoardService staffBoardService;

    protected Member inhaMember;
    protected Member gachonMember;
    protected Member inhaStaff;
    protected Member centerStaff;
    protected Board board;
    protected Board workbookBoard;
    protected University inha;
    protected University gachon;
    protected Branch branch;
    protected BoardComment comment;

    @BeforeEach
    public void setUp() {
        inha = findUniversityInha();
        gachon = findUniversityGachon();
        branch = createBranch();
        inhaStaff = createCampusStaff();
        centerStaff = createCenterStaff();
        inhaMember = createInhaMember();
        gachonMember = createGachonMember();
        board = createBoard();
        comment = createBoardComment();
        workbookBoard = createWorkbookBoard();
    }

    protected Member createCampusStaff() {
        return memberRepository.save(Member.builder()
                .id(UUID.randomUUID())
                .clientId("123456")
                .socialType(SocialType.KAKAO)
                .university(inha)
                .branch(branch)
                .role(Role.CAMPUS_STAFF)
                .name("김준석")
                .nickname("벡스")
                .semesterParts(createSemesterPartTHIRDFIFTH(inhaStaff))
                .build());
    }

    protected Member createCenterStaff() {
        return memberRepository.save(Member.builder()
                .id(UUID.randomUUID())
                .clientId("122222")
                .socialType(SocialType.KAKAO)
                .university(gachon)
                .branch(branch)
                .role(Role.CENTER_STAFF)
                .name("김연합")
                .nickname("센터")
                .build());
    }

    protected Member createInhaMember() {
        return memberRepository.save(Member.builder()
                .id(UUID.randomUUID())
                .clientId("11111")
                .socialType(SocialType.KAKAO)
                .university(inha)
                .branch(branch)
                .role(Role.MEMBER)
                .name("김수민")
                .nickname("루시")
                .semesterParts(createSemesterPartFIFTH(inhaMember))
                .build());
    }

    protected Member createGachonMember() {
        return memberRepository.save(Member.builder()
                .id(UUID.randomUUID())
                .clientId("999999")
                .socialType(SocialType.KAKAO)
                .university(gachon)
                .branch(branch)
                .role(Role.MEMBER)
                .name("심세원")
                .nickname("하나")
                .semesterParts(createSemesterPartFIFTH(gachonMember))
                .build());
    }
    protected List<SemesterPart> createSemesterPartTHIRDFIFTH(Member member) {
        List<SemesterPart> semesterParts = List.of(
                SemesterPart.builder().member(member).part(Part.SPRING).semester(Semester.FIFTH).build(),
                SemesterPart.builder().member(member).part(Part.ANDROID).semester(Semester.THIRD).build()
        );

        return semesterPartRepository.saveAll(semesterParts);
    }

    protected List<SemesterPart> createSemesterPartFIFTH(Member member) {
        List<SemesterPart> semesterParts = List.of(
                SemesterPart.builder().member(member).part(Part.SPRING).semester(Semester.FIFTH).build()
        );

        return semesterPartRepository.saveAll(semesterParts);
    }


    protected University findUniversityInha() {
        return universityRepository.findByName("인하대학교").get();
    }

    protected University findUniversityGachon() {
        return universityRepository.findByName("가천대학교").get();
    }

    protected Branch createBranch() {
        return branchRepository.findByName("GACI(가치)").get();
    }

    protected Board createBoard() {
        return boardRepository.save(Board.builder()
                .writer(inhaMember)
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
                .writer(inhaStaff)
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
                .writer(inhaStaff)
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


    protected void createBoards() {
        for (int i = 0; i < 20; i++) {
            boardRepository.save(Board.builder()
                    .writer(inhaMember)
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
                .writer(inhaMember)
                .board(board)
                .content("댓글")
                .build());
    }

    protected void createBoardComments() {
        for (int i = 0; i < 20; i++) {

            boardCommentRepository.save(BoardComment.builder()
                    .writer(inhaStaff)
                    .board(board)
                    .content("좋아요." + i)
                    .build());
        }
    }

    protected void setBoardHeart(Member member, Board board) {
        boardService.toggleBoardLike(member, board.getId());
    }


}
