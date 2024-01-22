package com.umc.networkingService.domain.board.service;

import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardFileService {
    void uploadBoardFiles(Board board, List<MultipartFile> files);
    void updateBoardFiles (Board board, List<MultipartFile> files);
    void deleteBoardFiles(Board board);
    List<BoardFile> findBoardFiles(Board board);
    String findThumbnailImage(Board board) ;

    }
