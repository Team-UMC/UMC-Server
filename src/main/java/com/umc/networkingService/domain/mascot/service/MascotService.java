package com.umc.networkingService.domain.mascot.service;

import com.umc.networkingService.domain.mascot.entity.Mascot;
import com.umc.networkingService.domain.mascot.entity.MascotType;

public interface MascotService {
    Mascot getMascotByStartLevel(int level, MascotType type);
}
