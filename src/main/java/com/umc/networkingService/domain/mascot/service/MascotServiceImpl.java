package com.umc.networkingService.domain.mascot.service;

import com.umc.networkingService.domain.mascot.entity.Mascot;
import com.umc.networkingService.domain.mascot.entity.MascotType;
import com.umc.networkingService.domain.mascot.repository.MascotRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.MascotErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MascotServiceImpl implements MascotService{

    private final MascotRepository mascotRepository;

    @Override
    public Mascot getMascotByStartLevel(int level, MascotType type) {
        Optional<Mascot> optionalMascot = mascotRepository.findByStartLevelAndType(level, type);
        return optionalMascot.orElseThrow(() -> new RestApiException(MascotErrorCode.EMPTY_MASCOT_LEVEL));
    }
}


