package com.umc.networkingService.domain.branch.converter;

import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.global.utils.S3FileComponent;

public class BranchConverter {

    public static Branch toBranch(BranchRequest.PostBranchDTO request, String imagePath){
        return Branch.builder()
                .name(request.getName())
                .description(request.getDescription())
                .image(imagePath)
                .semester(request.getSemester())
                .build();
    }
}
