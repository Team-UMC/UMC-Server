package com.umc.networkingService.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.networkingService.config.security.jwt.JwtTokenProvider;
import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCreateResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.repository.BoardImageRepository;
import com.umc.networkingService.domain.board.repository.BoardRepository;
import com.umc.networkingService.domain.board.service.BoardService;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.member.repository.MemberRepository;
import com.umc.networkingService.global.common.enums.Part;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.utils.S3FileComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
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

    @MockBean private BoardRepository boardRepository;

    @MockBean private MemberRepository memberRepository;

    @MockBean private S3FileComponent s3FileComponent;

    private Member member;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        member = createMember();
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
    public void boardCreateTest() throws Exception {
        //given
        BoardCreateRequest boardCreateRequest = BoardCreateRequest.builder()
                .title("제목")
                .content("내용")
                .boardType(BoardType.FREE)
                .hostType(HostType.CAMPUS)
                .build();

        BoardCreateResponse response = new BoardCreateResponse(member.getId());


        MockMultipartFile file1 = new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "file content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "file content".getBytes());
        MockMultipartFile request = new MockMultipartFile("request", "request", "application/json",  objectMapper.writeValueAsString(boardCreateRequest).getBytes(StandardCharsets.UTF_8));

        //static method 사용을 위한 mockedStatic 선언
        MockedStatic<Semester> semester = mockStatic(Semester.class);

        //when
        when(Semester.findActiveSemester()).thenReturn(Semester.FIFTH);
        when(boardRepository.save(any(Board.class))).thenReturn(createBoard());
        when(s3FileComponent.uploadFile(anyString(), any(MultipartFile.class))).thenReturn("mocked_file_url");
        when(boardService.createBoard(eq(member), any(BoardCreateRequest.class), anyList())).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        //then
        this.mockMvc.perform(
                        multipart("/boards")
                                .file(file1)
                                .file(file2)
                                .file(request)
                                .header("Authorization", accessToken))  // AccessToken을 실제 값으로 대체
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("COMMON200"))
                    .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                    .andExpect(jsonPath("$.result").exists());

        //mockedStatic closs
        semester.close();
    }

}
