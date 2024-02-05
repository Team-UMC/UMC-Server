package com.umc.networkingService.domain.mascot.service;

import com.umc.networkingService.domain.mascot.entity.Mascot;

public interface MascotService {
    Mascot getMascotByEndLevel(int endLevel);
}
