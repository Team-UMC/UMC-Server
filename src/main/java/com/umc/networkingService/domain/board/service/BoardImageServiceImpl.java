package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardImage;
import com.umc.networkingService.domain.board.mapper.BoardImageMapper;
import com.umc.networkingService.domain.board.repository.BoardImageRepository;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardImageServiceImpl implements BoardImageService {

    private final BoardImageRepository boardImageRepository;
    private final S3FileComponent s3FileComponent;

    @Override
    @Transactional
    public void uploadBoardImages (Board board, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            boardImageRepository.save(BoardImageMapper.toEntity(board, s3FileComponent.uploadFile("Board", file)));
        }
    }

    @Override
    @Transactional
    public void updateBoardImages (Board board, List<MultipartFile> files) {
        List<BoardImage> boardImages = boardImageRepository.findAllByBoard(board);


        if(!boardImages.isEmpty()) {
            for (BoardImage image: boardImages) {
                s3FileComponent.deleteFile(image.getUrl());
                boardImageRepository.deleteById(image.getId());
            }
        }
        if(files !=null)
            uploadBoardImages(board, files);
    }


}
