package com.umc.networkingService.domain.board.controller;

import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.request.comment.BoardCommentAddRequest;
import com.umc.networkingService.domain.board.dto.request.comment.BoardCommentUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.*;
import com.umc.networkingService.domain.board.dto.response.comment.BoardCommentIdResponse;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardPagingResponse;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.global.common.enums.Semester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Board 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest extends BoardControllerTestConfig {


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
        MockMultipartFile request = new MockMultipartFile("request", "request", "application/json", objectMapper.writeValueAsString(boardCreateRequest).getBytes(StandardCharsets.UTF_8));

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
                        multipart(PATCH, "/boards/{boardId}", board.getId())
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
    @DisplayName("특정 게시판의 게시글 목록 조회 API 테스트")
    public void showBoardsTest() throws Exception {
        // given
        BoardPagingResponse response = createMockBoardPagingResponse();

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
    @DisplayName("게시글 상세 조회 API 테스트")
    public void showBoardDetailTest() throws Exception {
        // given
        BoardDetailResponse response = BoardDetailResponse.builder()
                .hostType(board.getHostType())
                .boardType(board.getBoardType())
                .writer(board.getWriter().getNickname() + "/" + board.getWriter().getName())
                .profileImage(board.getWriter().getProfileImage())
                .semester(Semester.FIFTH)
                .title(board.getTitle())
                .content(board.getContent())
                .hitCount(board.getHitCount())
                .heartCount(board.getHeartCount())
                .boardFiles(new ArrayList<>())
                .commentCount(board.getCommentCount())
                .createdAt(board.getCreatedAt())
                .build();
        // when
        when(boardService.showBoardDetail(eq(member), eq(board.getId()))).thenReturn(response);
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

    @Test
    @DisplayName("게시글 검색 API 테스트")
    public void searchBoardTest() throws Exception {
        // given
        BoardSearchPagingResponse response = createMockBoardSearchPagingResponse();
        // when
        when(boardService.searchBoard(eq(member), any(String.class), any(Pageable.class))).thenReturn(response);
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
    @DisplayName("특정 게시글 좋아요/취소 테스트")
    public void toggleBoardHeart() throws Exception {
        //given
        BoardIdResponse response = new BoardIdResponse(board.getId());

        //when
        when(boardService.toggleBoardLike(eq(member), eq(board.getId()))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        //then
        this.mockMvc.perform(
                        post("/boards/{boardId}/heart", board.getId())
                                .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    @DisplayName("댓글 작성 성공 테스트")
    public void addBoardCommentTest() throws Exception {
        //given
        BoardCommentAddRequest boardCommentAddRequest = BoardCommentAddRequest.builder()
                .content("내용")
                .boardId(board.getId())
                .build();

        String request = objectMapper.writeValueAsString(boardCommentAddRequest);
        BoardCommentIdResponse response = new BoardCommentIdResponse(board.getId());

        //when
        when(boardCommentService.addBoardComment(eq(member), any(BoardCommentAddRequest.class))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        //then
        this.mockMvc.perform(
                        post("/boards/comments")
                                .header("Authorization", accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }


    @Test
    @DisplayName("댓글 수정 성공 테스트")
    public void updateBoardCommentTest() throws Exception {
        //given
        BoardCommentUpdateRequest boardCommentUpdateRequest = BoardCommentUpdateRequest.builder()
                .content("수정")
                .build();

        String request = objectMapper.writeValueAsString(boardCommentUpdateRequest);
        BoardCommentIdResponse response = new BoardCommentIdResponse(comment.getId());

        //when
        when(boardCommentService.updateBoardComment(eq(member), eq(comment.getId()), any(BoardCommentUpdateRequest.class))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        //then
        this.mockMvc.perform(
                        patch("/boards/comments/{commentId}", comment.getId())
                                .header("Authorization", accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }


    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    public void deleteBoardCommentTest() throws Exception {
        //given
        BoardCommentIdResponse response = new BoardCommentIdResponse(comment.getId());

        //when
        when(boardCommentService.deleteBoardComment(eq(member), eq(comment.getId()))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        //then
        this.mockMvc.perform(
                        delete("/boards/comments/{commentId}", comment.getId())
                                .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    @DisplayName("App용 특정 멤버가 작성한 게시글 목록 조회/검색 API 테스트")
    public void showMemberBoardsTestForApp() throws Exception {
        // given
        MyBoardPagingResponse response = createMockMyBoardPagingResponse();

        // when
        when(boardService.showBoardsByMemberForApp(eq(member), any(String.class), any(Pageable.class))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/boards/member/app")
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
    @DisplayName("WEB용 특정 멤버가 작성한 게시글 목록 조회/검색 API 테스트")
    public void showMemberBoardsTestForWeb() throws Exception {
        // given
        MyBoardPagingResponse response = createMockMyBoardPagingResponse();

        // when
        when(boardService.showBoardsByMemberForWeb(eq(member), any(HostType.class), any(BoardType.class), any(String.class), any(Pageable.class))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));

        // then
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/boards/member/web")
                        .param("host", "CENTER")
                        .param("board", "FREE")
                        .param("keyword", "")
                        .param("page", "0")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }



    @Test
    @DisplayName("운영진용 교내 공지사항 조회 테스트")
    public void showStaffNoticesTest() throws Exception {
        //given
        BoardNoticePagingResponse response = createMockMyBoardNoticePagingResponse();
        //when
        when(staffBoardService.showNotices(eq(member), any(HostType.class), any(String.class), any(Pageable.class))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));
        // then

        this.mockMvc.perform(get("/staff/boards/notices")
                        .param("host", "CENTER")
                        .param("keyword", "")
                        .param("page", "0")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    @DisplayName("운영진용 핀설정 테스트")
    public void setStaffNoticePinTest() throws Exception {
        //given
       BoardIdResponse response = new BoardIdResponse(board.getId());
        //when
        when(staffBoardService.toggleNoticePin(eq(member),eq(board.getId()), any(boolean.class))).thenReturn(response);
        when(memberRepository.findById(any(UUID.class))).thenReturn(Optional.of(member));
        // then

        mockMvc.perform(patch("/staff/boards/notices/{boardId}/pin",board.getId())
                        .param("isPinned", "true")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result").exists());
    }
}



