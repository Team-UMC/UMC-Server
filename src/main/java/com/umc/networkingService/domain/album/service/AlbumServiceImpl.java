package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.*;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumHeart;
import com.umc.networkingService.domain.album.entity.AlbumImage;
import com.umc.networkingService.domain.album.mapper.AlbumHeartMapper;
import com.umc.networkingService.domain.album.mapper.AlbumMapper;
import com.umc.networkingService.domain.album.repository.AlbumHeartRepository;
import com.umc.networkingService.domain.album.repository.AlbumRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.AlbumErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService{

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final AlbumImageService albumImageService;
    private final AlbumHeartRepository albumHeartRepository;
    private final AlbumHeartMapper albumHeartMapper;

    private final MemberService memberService;

    // 사진첩 생성 함수
    @Override
    public AlbumIdResponse createAlbum(
            Member member, AlbumCreateRequest request, List<MultipartFile> albumImages) {

        Album album = albumRepository.save(albumMapper.toAlbum(member, request));

        if (albumImages != null)
            albumImageService.createAlbumImages(album, albumImages);

        return new AlbumIdResponse(album.getId());
    }

    // 사진첩 수정 함수
    @Override
    @Transactional
    public AlbumIdResponse updateAlbum(
            Member member, UUID albumId, AlbumUpdateRequest request, List<MultipartFile> albumImages) {

        Album album = loadEntity(albumId);

        // 본인만 삭제 가능
        if (!album.getWriter().getId().equals(member.getId()))
            throw new RestApiException(AlbumErrorCode.NO_AUTHORIZATION_ALBUM);

        album.updateAlbum(request);

        if (albumImages != null)
            albumImageService.updateAlbumImages(album, albumImages);

        return new AlbumIdResponse(album.getId());
    }

    @Override
    @Transactional
    public AlbumIdResponse deleteAlbum(Member member, UUID albumId) {

        Album album = loadEntity(albumId);

        // 로그인한 member와 writer가 같지 않을 경우 삭제 불가능
        if (!album.getWriter().getId().equals(member.getId())) {
            // 상위 staff 역할을 가진 경우 삭제 가능
            if(member.getRole().getPriority() < album.getWriter().getRole().getPriority())
                throw new RestApiException(AlbumErrorCode.NO_AUTHORIZATION_ALBUM);
        }

        albumImageService.deleteAlbumImages(album);
        album.delete();

        return new AlbumIdResponse(album.getId());
    }

    @Override
    public AlbumPagingResponse<AlbumInquiryResponse> inquiryAlbums(Member loginMember, Semester semester, Pageable pageable) {
        Member member = memberService.loadEntity(loginMember.getId());

        if (semester == null) {
            Page<Album> albumPage = albumRepository.findAllByWriter_University(member.getUniversity(), pageable);
            return getAlbumPagingResponse(albumPage);
        } else {
            Page<Album> albumPage = albumRepository.findAllByWriter_UniversityAndSemester(member.getUniversity(), semester, pageable);
            return getAlbumPagingResponse(albumPage);
        }
    }

    @Override
    public AlbumPagingResponse<AlbumInquiryResponse> searchAlbums(
            Member loginMember, String keyword, Pageable pageable) {

        Member member = memberService.loadEntity(loginMember.getId());

        Page<Album> albumPage = albumRepository.findAllByTitleContainsOrContentContains(keyword, member.getUniversity(), pageable);

        return getAlbumPagingResponse(albumPage);
    }

    @Override
    public AlbumPagingResponse<AlbumInquiryFeaturedResponse> inquiryAlbumsFeatured(Member loginMember, Pageable pageable) {

        Member member = memberService.loadEntity(loginMember.getId());

        Page<Album> albumPage = albumRepository.findAllByWriter_UniversityOrderByHeartCount(member.getUniversity(), pageable);

        return albumMapper.toAlbumPagingResponse(
                albumPage,
                albumPage.stream()
                        .map(album -> albumMapper.toAlbumPageFeaturedResponse(
                                album, albumImageService.findThumbnailImage(album)
                        )).toList());
    }

    @Override
    @Transactional
    public AlbumDetailResponse inquiryAlbumDetail(Member member, UUID albumId) {

        Album album = loadEntity(albumId);

        // 조회수 증가
        album.increaseHitCount();

        // 앨범 상세 정보 매핑
        return albumMapper.toAlbumDetailResponse(
                album,
                findAlbumImageUrls(album),
                albumMapper.toWriterInfo(member, memberService.findRepresentativePosition(member)),
                checkAlbumLikeByMember(member,album),
                album.getId().equals(member.getId()));
    }

    @Override
    @Transactional
    public AlbumHeartResponse toggleAlbumLike(Member member, UUID albumId) {
        Album album = loadEntity(albumId);

        AlbumHeart albumHeart = albumHeartRepository.findByMemberAndAlbum(member, album)
                .orElse(albumHeartRepository.save(
                                albumHeartMapper.toAlbumHeartEntity(album, member)));

        albumHeart.toggleHeart();
        album.setHeartCount(albumHeart.isChecked());

        return new AlbumHeartResponse(albumHeart.isChecked(), album.getHeartCount());
    }

    @Override
    public Album loadEntity(UUID albumId) {
        return albumRepository.findById(albumId).orElseThrow(() -> new RestApiException(
                AlbumErrorCode.EMPTY_ALBUM));
    }

    private AlbumPagingResponse<AlbumInquiryResponse> getAlbumPagingResponse(Page<Album> albumPage) {
        return albumMapper.toAlbumPagingResponse(
                albumPage,
                albumPage.stream()
                        .map(album -> albumMapper.toAlbumInquiryResponse(
                                album,
                                albumMapper.toWriterInfo(album.getWriter(), memberService.findRepresentativePosition(album.getWriter())),
                                albumImageService.findThumbnailImage(album),
                                getImageCnt(album)))
                        .toList());
    }

    private int getImageCnt(Album album) {
        return albumImageService.countAlbumImages(album);
    }

    private boolean checkAlbumLikeByMember(Member member, Album album) {
        return albumHeartRepository.findByMemberAndAlbum(member, album)
                .map(AlbumHeart::isChecked)
                .orElse(false);
    }

    private List<String> findAlbumImageUrls(Album album) {
        return albumImageService.findAlbumImages(album).stream()
                .map(AlbumImage::getUrl)
                .toList();
    }
}
