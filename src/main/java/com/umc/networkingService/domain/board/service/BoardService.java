package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCreateResponse;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    BoardCreateResponse boardCreate(Member member, BoardCreateRequest request, List<MultipartFile> files);
}
