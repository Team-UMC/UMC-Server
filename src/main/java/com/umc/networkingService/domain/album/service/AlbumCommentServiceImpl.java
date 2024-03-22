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

        if (!checkUpdateAuthorization(comment.getWriter(), member)) {
            throw new RestApiException(AlbumCommentErrorCode.NO_AUTHORIZATION_ALBUM_COMMENT);
        }

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

        if (!checkDeleteAuthorization(comment.getWriter(), member)) {
            throw new RestApiException(AlbumCommentErrorCode.NO_AUTHORIZATION_ALBUM_COMMENT);
        }

        album.decreaseCommentCount();

        handleCommentDeletion(comment);

        return new AlbumCommentResponse(comment.getId(), album.getCommentCount());
    }

    private boolean checkUpdateAuthorization(Member writer, Member loginMember) {
        return checkWriter(writer, loginMember);
    }

    private boolean checkDeleteAuthorization(Member writer, Member loginMember) {
        // 작성자이거나 더 높은 권한을 가진 경우 삭제 권한 부여
        return checkWriter(writer, loginMember) || checkHighStaff(writer, loginMember);
    }

    private boolean checkWriter(Member writer, Member loginMember) {
        return writer.getId().equals(loginMember.getId());
    }

    private boolean checkHighStaff(Member writer, Member loginMember) {
        return loginMember.getRole().getPriority() < writer.getRole().getPriority();
    }

    private void handleCommentDeletion(AlbumComment comment) {
        if (albumCommentRepository.existsByParentComment(comment)) {
            comment.deleteComment();
        } else {
            comment.delete();
        }
    }

    @Override
    public AlbumComment loadEntity(UUID commentId) {
        return albumCommentRepository.findById(commentId).orElseThrow(
                () -> new RestApiException(AlbumCommentErrorCode.EMPTY_ALBUM_COMMENT));
    }
}
