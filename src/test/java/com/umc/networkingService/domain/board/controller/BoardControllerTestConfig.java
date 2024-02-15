package com.umc.networkingService.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.board.dto.response.BoardResponse;
import com.umc.networkingService.domain.board.dto.response.MyBoardResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardComment;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.service.BoardCommentService;
import com.umc.networkingService.domain.board.service.BoardService;
import com.umc.networkingService.domain.board.service.StaffBoardService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.domain.member.repository.SemesterPartRepository;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BoardControllerTestConfig {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected JwtTokenProvider jwtTokenProvider;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected BoardService boardService;
    @MockBean
    protected MemberRepository memberRepository;
    @MockBean
    protected BoardCommentService boardCommentService;
    @MockBean
    protected SemesterPartRepository semesterPartRepository;
    @MockBean
    protected StaffBoardService staffBoardService;


    protected Member member;
    protected Board board;
    protected BoardComment comment;
    protected String accessToken;
    protected String refreshToken;

    @BeforeEach
    public void setUp() {
        member = createMember();
        board = createBoard();
        comment = createBoardComment();
        setToken(member);
    }


    private void setToken(Member member) {
        accessToken = jwtTokenProvider.generateAccessToken(member.getId());
        refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());
    }

    protected Member createMember() {
        return Member.builder()
                .id(UUID.randomUUID())
                .clientId("123456")
                .socialType(SocialType.KAKAO)
                .role(Role.CENTER_STAFF)
                .name("김준석")
                .nickname("벡스")
                .semesterParts(createSemesterPart(member))
                .build();
    }

    protected List<SemesterPart> createSemesterPart(Member member) {
        List<SemesterPart> semesterParts = List.of(
                SemesterPart.builder().member(member).part(Part.ANDROID).semester(Semester.THIRD).build(),
                SemesterPart.builder().member(member).part(Part.SPRING).semester(Semester.FOURTH).build()
        );

        return semesterParts;
    }


    protected Board createBoard() {
        return Board.builder()
                .id(UUID.randomUUID())
                .writer(member)
                .title("제목")
                .content("내용")
                .semesterPermission(List.of(Semester.FIFTH))
                .hostType(HostType.CAMPUS)
                .boardType(BoardType.FREE)
                .build();

    }
    protected BoardComment createBoardComment() {
        return BoardComment.builder()
                .id(UUID.randomUUID())
                .writer(member)
                .board(board)
                .content("신나!")
                .build();
    }



    //가상의 BoardPageInfos 생성
    protected BoardResponse.BoardPageInfos createMockBoardPageInfos() {
        List<BoardResponse.BoardPageElement> boardPageResponses = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BoardResponse.BoardPageElement boardPageResponse = BoardResponse.BoardPageElement.builder()
                    .title("제목")
                    .content("내용")
                    .writer("루시/김수민")
                    .hitCount(1)
                    .commentCount(1)
                    .heartCount(1)
                    .profileImage(".../img")
                    .createdAt(LocalDateTime.parse("2024-01-16T14:20:15"))
                    .build();
            boardPageResponses.add(boardPageResponse);
        }


        // 가상의 페이징 정보 설정
        return BoardResponse.BoardPageInfos.builder()
                .page(1)
                .totalPages(3)
                .totalElements(30)
                .boardPageElements(boardPageResponses)
                .isFirst(true)
                .isLast(false)
                .build();
    }

    protected BoardResponse.BoardSearchPageInfos createMockBoardSearchPageInfos() {
        List<BoardResponse.BoardSearchPageElement> boardSearchPageElements = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BoardResponse.BoardSearchPageElement boardSearchPageResponse = BoardResponse.BoardSearchPageElement.builder()
                    .boardType(BoardType.FREE)
                    .hostType(HostType.CAMPUS)
                    .boardId(UUID.randomUUID())
                    .title("제목")
                    .content("내용")
                    .writer("루시/김수민")
                    .hitCount(1)
                    .commentCount(1)
                    .heartCount(1)
                    .profileImage(".../img")
                    .createdAt(LocalDateTime.parse("2024-01-16T14:20:15"))
                    .build();
            boardSearchPageElements.add(boardSearchPageResponse);
        }


        // 가상의 페이징 정보 설정
        return BoardResponse.BoardSearchPageInfos.builder()
                .page(1)
                .totalPages(3)
                .totalElements(30)
                .boardSearchPageElements(boardSearchPageElements)
                .isFirst(true)
                .isLast(false)
                .build();
    }

    protected MyBoardResponse.MyBoardPageInfos createMockMyBoardResponse() {
        List<MyBoardResponse.MyBoardPageElement> myBoardPageElements = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MyBoardResponse.MyBoardPageElement myBoardPageElement = MyBoardResponse.MyBoardPageElement.builder()
                    .boardType(BoardType.FREE)
                    .hostType(HostType.CAMPUS)
                    .boardId(UUID.randomUUID())
                    .title("제목")
                    .hitCount(1)
                    .heartCount(1)
                    .createdAt(LocalDateTime.parse("2024-01-16T14:20:15"))
                    .build();
            myBoardPageElements.add(myBoardPageElement);
        }


        // 가상의 페이징 정보 설정
        return MyBoardResponse.MyBoardPageInfos.builder()
                .page(1)
                .totalPages(3)
                .totalElements(30)
                .myBoardPageElements(myBoardPageElements)
                .isFirst(true)
                .isLast(false)
                .build();
    }


    protected BoardResponse.NoticePageInfos createMockNoticePageInfos() {
        List<BoardResponse.NoticePageElement> noticePageElements = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BoardResponse.NoticePageElement noticePageElement = BoardResponse.NoticePageElement.builder()
                    .hostType(HostType.CAMPUS)
                    .boardId(UUID.randomUUID())
                    .title("제목")
                    .hitCount(1)
                    .createdAt(LocalDateTime.parse("2024-01-16T14:20:15"))
                    .isFixed(false)
                    .writer("루시/김수민")
                    .build();
            noticePageElements.add(noticePageElement);
        }


        // 가상의 페이징 정보 설정
        return BoardResponse.NoticePageInfos.builder()
                .page(1)
                .totalPages(3)
                .totalElements(30)
                .noticePageElements(noticePageElements)
                .isFirst(true)
                .isLast(false)
                .build();
    }




}
