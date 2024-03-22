package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCommentCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumCommentUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumCommentResponse;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumComment;
import com.umc.networkingService.domain.album.mapper.AlbumCommentMapper;
import com.umc.networkingService.domain.album.repository.AlbumCommentRepository;
import com.umc.networkingService.domain.album.repository.AlbumRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.AlbumCommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumCommentServiceImpl implements AlbumCommentService{

    private final AlbumCommentRepository albumCommentRepository;
    private final AlbumCommentMapper albumCommentMapper;
    private final AlbumService aLbumService;

    @Override
    @Transactional
    public AlbumCommentResponse createAlbumComment(Member member, UUID commentId, AlbumCommentCreateRequest request) {

        Album album = aLbumService.loadEntity(request.getAlbumId());

        AlbumComment parentComment = Optional.ofNullable(commentId)
                .map(this::loadEntity)
                .orElse(null);

        AlbumComment comment = albumCommentRepository.save(
                albumCommentMapper.createAlbumComment(member, parentComment, album, request.getContent()));

        album.increaseCommentCount();

        return new AlbumCommentResponse(comment.getId(), album.getCommentCount());
    }

    @Override
    @Transactional
    public AlbumCommentResponse updateAlbumComment(Member member, UUID commentId, AlbumCommentUpdateRequest request) {

        AlbumComment comment = loadEntity(commentId);

        checkWriter(comment.getWriter(),member);

        comment.update(request);

        return new AlbumCommentResponse(
                comment.getId(),
                aLbumService.loadEntity(comment.getAlbum().getId()).getCommentCount());
    }

    @Override
    @Transactional
    public AlbumCommentResponse deleteAlbumComment(Member member, UUID commentId) {

        AlbumComment comment = loadEntity(commentId);
        Album album = comment.getAlbum();

        checkWriter(comment.getWriter(), member);
        checkHighStaff(comment.getWriter(), member);

        album.decreaseCommentCount();
        comment.delete();

        return new AlbumCommentResponse(comment.getId(), album.getCommentCount());
    }

    @Override
    public AlbumComment loadEntity(UUID commentId) {
        return albumCommentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(AlbumCommentErrorCode.EMPTY_ALBUM_COMMENT));
    }

    private void checkWriter(Member writer, Member loginMember) {
        if (!loginMember.getId().equals(writer.getId()))
            throw new RestApiException(AlbumCommentErrorCode.NO_AUTHORIZATION_ALBUM_COMMENT);
    }

    private void checkHighStaff(Member writer, Member loginMember) {
        if (loginMember.getRole().getPriority() >= writer.getRole().getPriority())
            throw new RestApiException(AlbumCommentErrorCode.NO_AUTHORIZATION_ALBUM_COMMENT);
    }
}
