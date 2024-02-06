package com.umc.networkingService.domain.mascot.service;

import com.umc.networkingService.domain.mascot.entity.Mascot;
import com.umc.networkingService.domain.mascot.repository.MascotRepository;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MascotServiceImpl implements MascotService{

    private final MascotRepository mascotRepository;

    @Override
    public Mascot getMascotByEndLevel(int endLevel){
        Optional<Mascot> optionalMascot = mascotRepository.findByEndLevel(endLevel);
        return optionalMascot.orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_MASCOT_LEVEL));
    }
}


