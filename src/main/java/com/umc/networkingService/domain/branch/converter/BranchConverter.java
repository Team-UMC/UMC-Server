package com.umc.networkingService.domain.branch.converter;

import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.utils.S3FileComponent;

import java.util.List;
import java.util.stream.Collectors;

public class BranchConverter {

    public static Branch toBranch(
            BranchRequest.PostBranchDTO request
            ,String imagePath)
    {
        return Branch.builder()
                .name(request.getName())
                .description(request.getDescription())
                .image(imagePath)
                .semester(request.getSemester())
                .build();
    }

    public static BranchResponse.JoinBranchListDTO toJoinBranchListDTO(
            List<Branch> branchList
    ){
        return BranchResponse.JoinBranchListDTO.builder()
                .branchList(branchList.stream()
                        .map(branch -> BranchResponse.BranchDTO.builder()
                                .branchId(branch.getId())
                                .name(branch.getName())
                                .description(branch.getDescription())
                                .image(branch.getImage())
                                .semester(branch.getSemester())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static BranchResponse.JoinBranchDetailDTO toJoinBranchDetailDTO(
            List<University> universityList
    ){
        return BranchResponse.JoinBranchDetailDTO.builder()
                .universityList(universityList.stream()
                        .map(university -> BranchResponse.BranchUniversityDTO.builder()
                                .universityId(university.getId())
                                .name(university.getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
