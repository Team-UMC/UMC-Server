package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCommentCreateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumCommentIdResponse;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumComment;
import com.umc.networkingService.domain.album.mapper.AlbumCommentMapper;
import com.umc.networkingService.domain.album.repository.AlbumCommentRepository;
import com.umc.networkingService.domain.album.repository.AlbumRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlbumCommentServiceImpl implements AlbumCommentService{

    private final AlbumCommentRepository albumCommentRepository;
    private final AlbumRepository albumRepository;
    private final AlbumService albumService;
    private final AlbumCommentMapper albumCommentMapper;

    @Override
    public AlbumCommentIdResponse createAlbumComment(Member member, AlbumCommentCreateRequest request) {
        Album album = albumRepository.findById(request.getAlbumId()).orElseThrow(() -> new RestApiException(
                ErrorCode.EMPTY_ALBUM));

        AlbumComment comment = albumCommentRepository.save(
                albumCommentMapper.createAlbumComment(member, album, request));

        album.increaseCommentCount();

        return new AlbumCommentIdResponse(comment.getId());
    }
}
