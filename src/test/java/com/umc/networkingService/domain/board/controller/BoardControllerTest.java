package com.umc.networkingService.domain.board.controller;

import com.amazonaws.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.PATCH;
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

}
