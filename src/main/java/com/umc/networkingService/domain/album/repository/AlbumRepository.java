package com.umc.networkingService.domain.album.repository;

import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.enums.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlbumRepository extends JpaRepository<Album, UUID>, AlbumRepositoryCustom {

    Page<Album> findAllByWriter_UniversityAndSemester(University writer_university, Semester semester, Pageable pageable);
}