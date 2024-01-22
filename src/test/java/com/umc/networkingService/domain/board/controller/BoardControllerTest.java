package com.umc.networkingService.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardDetailResponse;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagePostResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.service.BoardService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Board 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private BoardService boardService;
    @MockBean private MemberRepository memberRepository;
    private Member member;
    private Board board;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        member = createMember();
        board = createBoard();
        accessToken = jwtTokenProvider.generateToken(member.getId()).getAccessToken();
    }

    private Member createMember() {
        return Member.builder()
                .id(UUID.randomUUID())
                .clientId("123456")
                .socialType(SocialType.KAKAO)
                .role(Role.MEMBER)
                .name("김준석")
                .nickname("벡스")
                .part(List.of(Part.SPRING))
                .semester(List.of(Semester.THIRD, Semester.FOURTH, Semester.FIFTH))
                .build();
    }

    private Board createBoard() {
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

    @Test
    @DisplayName("게시글 생성 API 테스트")
    public void createBoardTest() throws Exception {
        //given
        BoardCreateRequest boardCreateRequest = BoardCreateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType(BoardType.FREE)
                .hostType(HostType.CAMPUS)
                .build();

        BoardIdResponse response = new BoardIdResponse(board.getId());


        MockMultipartFile file1 = new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes());
        MockMultipartFile request = new MockMultipartFile("request", "request", "application/json",  objectMapper.writeValueAsString(boardCreateRequest).getBytes(StandardCharsets.UTF_8));

        //when
        when(boardService.createBoard(eq(member), any(BoardCreateRequest.class), anyList())).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        //then
        this.mockMvc.perform(
                        multipart("/boards")
                                .file(file1)
                                .file(file2)
                                .file(request)
                                .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());

    }
    @Test
    @DisplayName("게시글 수정 API 테스트")
    public void updateBoardTest() throws Exception {
        //given
        BoardUpdateRequest boardUpdateRequest = BoardUpdateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType(BoardType.FREE)
                .hostType(HostType.CAMPUS)
                .build();

        BoardIdResponse response = new BoardIdResponse(board.getId());

        MockMultipartFile file1 = new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes());
        MockMultipartFile request = new MockMultipartFile("request", "request", "application/json",
                objectMapper.writeValueAsString(boardUpdateRequest).getBytes(StandardCharsets.UTF_8));

        //when
        when(boardService.updateBoard(eq(member), eq(board.getId()), any(BoardUpdateRequest.class), anyList())).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        //then
        this.mockMvc.perform(
                        multipart(PATCH,"/boards/{boardId}",board.getId())
                                .file(file1)
                                .file(request)
                                .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    @DisplayName("게시글 삭제 API 테스트")
    public void deleteBoardTest() throws Exception {
        //given
        BoardIdResponse response = new BoardIdResponse(board.getId());

        //when
        when(boardService.deleteBoard(eq(member), eq(board.getId()))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        //then
        this.mockMvc.perform(
                        delete("/boards/{boardId}", board.getId())
                                .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());

    }

    @Test
    @DisplayName("게시글 검색 테스트")
    public void searchBoardTest() throws Exception {
        // given
        BoardPagingResponse response = createMockBoardPagingResponse(); // 테스트를 위한 가상의 게시글 데이터 생성
        // when
        when(boardService.searchBoard(eq(member), any(String.class),any(Pageable.class))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/boards/search")
                        .param("keyword", "데모데이")
                        .param("page", "0")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }


    @Test
    @DisplayName("특정 게시판의 게시글 목록 조회 테스트")
    public void showBoardsTest() throws Exception {
        // given
        BoardPagingResponse response = createMockBoardPagingResponse(); // 테스트를 위한 가상의 게시글 데이터 생성
        // when
        when(boardService.showBoards(eq(member), any(HostType.class), any(BoardType.class), any(Pageable.class))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/boards")
                        .param("host", "CAMPUS")
                        .param("board", "NOTICE")
                        .param("page", "0")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    @DisplayName("게시글 상세 조회 테스트")
    public void showBoardDetailTest() throws Exception {
        // given
        BoardDetailResponse response = BoardDetailResponse.builder()
                .hostType(board.getHostType())
                .boardType(board.getBoardType())
                .writer(board.getWriter().getNickname()+"/"+board.getWriter().getName())
                .profileImage(board.getWriter().getProfileImage())
                .semester(board.getWriter().getRecentSemester())
                .title(board.getTitle())
                .content(board.getContent())
                .hitCount(board.getHitCount())
                .heartCount(board.getHeartCount())
                .boardFiles(new ArrayList<>())
                .commentCount(board.getCommentCount())
                .createdAt(board.getCreatedAt())
                .build();
        // when
        when(boardService.showBoardDetail(eq(member),eq(board.getId()))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/boards/{boardId}", board.getId())
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }



    public BoardPagingResponse createMockBoardPagingResponse() {

        // 가상의 BoardPostResponse 리스트 생성
        List<BoardPagePostResponse> boardPagePostResponses = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BoardPagePostResponse boardPagePostResponse = BoardPagePostResponse.builder()
                    .title("제목")
                    .content("내용")
                    .writer("루시/김수민")
                    .hitCount(1)
                    .commentCount(1)
                    .heartCount(1)
                    .profileImage(".../img")
                    .createdAt(LocalDateTime.parse("2024-01-16T14:20:15"))
                    .build();
            boardPagePostResponses.add(boardPagePostResponse);
        }


        // 가상의 페이징 정보 설정
        return BoardPagingResponse.builder()
                .page(1)
                .totalPages(3)
                .totalElements(30)
                .boardPagePostResponses(boardPagePostResponses)
                .isFirst(true)
                .isLast(false)
                .build();
    }

}
