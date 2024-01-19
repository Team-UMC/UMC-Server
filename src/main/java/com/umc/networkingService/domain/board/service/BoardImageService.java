package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.entity.Board;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardImageService {
    void uploadBoardImages(Board board, List<MultipartFile> files);
    void updateBoardImages (Board board, List<MultipartFile> files);
    void deleteBoardImages(Board board);

    }
