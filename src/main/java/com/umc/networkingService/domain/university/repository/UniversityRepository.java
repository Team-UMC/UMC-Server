package com.umc.networkingService.domain.university.repository;

import com.umc.networkingService.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UniversityRepository extends JpaRepository<University, UUID> {
    Optional<University> findByName(String name);
}
