package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardFile;
import com.umc.networkingService.domain.board.mapper.BoardFileMapper;
import com.umc.networkingService.domain.board.repository.BoardFileRepository;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardFileServiceImpl implements BoardFileService {

    private final BoardFileRepository boardFileRepository;
    private final BoardFileMapper boardFileMapper;
    private final S3FileComponent s3FileComponent;

    @Override
    @Transactional
    public void uploadBoardFiles(Board board, List<MultipartFile> files) {
        files.forEach(file -> boardFileRepository.save(boardFileMapper
                .toBoardFileEntity(board, s3FileComponent.uploadFile("Board", file))));
    }

    @Override
    @Transactional
    public void updateBoardFiles(Board board, List<MultipartFile> files) {

        List<BoardFile> boardFiles = findBoardFiles(board);

        boardFiles.forEach(file -> {
            s3FileComponent.deleteFile(file.getUrl());
            boardFileRepository.deleteById(file.getId());
        });

        if (files != null)
            uploadBoardFiles(board, files);
    }

    @Override
    @Transactional
    public void deleteBoardFiles(Board board) {
        List<BoardFile> boardFiles = findBoardFiles(board);

        boardFiles.forEach(BoardFile::delete);
    }

    @Override
    public List<BoardFile> findBoardFiles(Board board) {
        return boardFileRepository.findAllByBoard(board);
    }


    @Override
    public String findThumbnailImage(Board board) {
        //이미지 파일이 있으면 가장 첫번째 이미지를 thumbnail로 반환, 없으면 null 반환
        return findBoardFiles(board).stream()
                .filter(file -> isImageFile(file.getUrl()))
                .findFirst()
                .map(BoardFile::getUrl)
                .orElse(null);
    }

    //이미지파일인지 확인
    public static boolean isImageFile(String url) {
        String lowerCaseUrl = url.toLowerCase();
        return lowerCaseUrl.endsWith(".jpg") || lowerCaseUrl.endsWith(".jpeg") ||
                lowerCaseUrl.endsWith(".png") || lowerCaseUrl.endsWith(".gif");
    }

}
