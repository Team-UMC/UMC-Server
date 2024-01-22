package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardFile;
import com.umc.networkingService.domain.board.mapper.BoardImageMapper;
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
    private final S3FileComponent s3FileComponent;

    @Override
    @Transactional
    public void uploadBoardFiles (Board board, List<MultipartFile> files) {
        files.forEach(file -> boardFileRepository.save(BoardImageMapper
                .toEntity(board, s3FileComponent.uploadFile("Board", file))));
    }

    @Override
    @Transactional
    public void updateBoardFiles (Board board, List<MultipartFile> files) {

        List<BoardFile> boardFiles = boardFileRepository.findAllByBoard(board);

        boardFiles.forEach(file -> {
            s3FileComponent.deleteFile(file.getUrl());
            boardFileRepository.deleteById(file.getId());
        });

        if(files !=null)
            uploadBoardFiles(board, files);
    }

    @Override
    @Transactional
    public void deleteBoardFiles(Board board) {
        List<BoardFile> boardFiles = boardFileRepository.findAllByBoard(board);

        boardFiles.forEach(BoardFile::delete);
    }

    public List<BoardFile> findBoardFiles(Board board) {
        return boardFileRepository.findAllByBoard(board);
    }


}
