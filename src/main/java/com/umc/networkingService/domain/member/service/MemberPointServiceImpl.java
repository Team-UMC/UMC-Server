package com.umc.networkingService.domain.member.service;

import com.umc.networkingService.domain.member.entity.MemberPoint;
import com.umc.networkingService.domain.member.repository.MemberPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPointServiceImpl implements MemberPointService{
    private final MemberPointRepository memberPointRepository;

    @Override
    @Transactional
    public MemberPoint saveEntity(MemberPoint memberPoint) {
        return memberPointRepository.save(memberPoint);
    }
}
