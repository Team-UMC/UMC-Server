package com.umc.networkingService.domain.mascot.service;

import com.umc.networkingService.domain.mascot.entity.Mascot;
import com.umc.networkingService.domain.mascot.repository.MascotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MascotServiceImpl implements MascotService{

    private final MascotRepository mascotRepository;

    @Override
    public Mascot getMascotByEndLevel(int endLevel){
        return mascotRepository.findByEndLevel(endLevel);
    }

}
