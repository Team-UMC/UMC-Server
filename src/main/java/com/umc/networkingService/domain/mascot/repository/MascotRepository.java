package com.umc.networkingService.domain.mascot.repository;

import com.umc.networkingService.domain.mascot.entity.Mascot;
import com.umc.networkingService.domain.mascot.entity.MascotType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MascotRepository  extends JpaRepository<Mascot, UUID> {
    Optional<Mascot> findByStartLevelAndType(int startLevel, MascotType type);
    boolean existsByStartLevelAndType(int startLevel, MascotType type);
}
