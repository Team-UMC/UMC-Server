package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BoardService {
    BoardIdResponse createBoard(Member member, BoardCreateRequest request, List<MultipartFile> files);
    BoardIdResponse updateBoard(Member member, UUID boardId, BoardUpdateRequest request, List<MultipartFile> files);

}
