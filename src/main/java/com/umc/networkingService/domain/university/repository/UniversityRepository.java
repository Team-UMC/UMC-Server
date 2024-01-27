package com.umc.networkingService.domain.university.repository;

import com.umc.networkingService.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import java.util.UUID;

public interface UniversityRepository extends JpaRepository<University, UUID> {
=======
import java.util.Optional;
import java.util.UUID;

public interface UniversityRepository extends JpaRepository<University, UUID> {
    Optional<University> findByName(String name);
>>>>>>> 9a5c384ff89a20278f29f42c6165fd78f74392cf
}
