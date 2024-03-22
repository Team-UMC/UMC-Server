package com.umc.networkingService.domain.album.repository;

import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.enums.Semester;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlbumRepository extends JpaRepository<Album, UUID>, AlbumRepositoryCustom {

    Page<Album> findAllByWriter_UniversityAndSemester(University writer_university, Semester semester, Pageable pageable);
    Page<Album> findAllByWriter_UniversityOrderByHeartCount(University writer_university, Pageable pageable);
    @Query("SELECT a FROM Album a WHERE (a.title LIKE %:keyword% OR a.content LIKE %:keyword%) AND a.writer.university = :university")
    Page<Album> findAllByTitleContainsOrContentContains(@Param("keyword") String keyword, @Param("university") University university, Pageable pageable);
    boolean existsByTitle(String title);
}