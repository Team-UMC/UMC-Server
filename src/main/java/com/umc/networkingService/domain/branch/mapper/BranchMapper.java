package com.umc.networkingService.domain.branch.mapper;

import com.umc.networkingService.domain.branch.dto.request.BranchRequest;
import com.umc.networkingService.domain.branch.dto.response.BranchResponse;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.university.entity.University;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BranchMapper {

    public static Branch toBranch(
            BranchRequest.BranchInfoDTO request
            ,String imagePath)
    {
        return Branch.builder()
                .name(request.getName())
                .description(request.getDescription())
                .image(imagePath)
                .semester(request.getSemester())
                .build();
    }

    public static BranchResponse.JoinBranchs toJoinBranchListDTO(
            List<Branch> branchList
    ){
        return BranchResponse.JoinBranchs.builder()
                .branchs(branchList.stream()
                        .map(branch -> BranchResponse.JoinBranch.builder()
                                .branchId(branch.getId())
                                .name(branch.getName())
                                .description(branch.getDescription())
                                .image(branch.getImage())
                                .semester(branch.getSemester())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static BranchResponse.JoinBranchDetails toJoinBranchDetailDTO(
            List<University> universityList
    ){
        return BranchResponse.JoinBranchDetails.builder()
                .universities(universityList.stream()
                        .map(university -> BranchResponse.JoinBranchDetail.builder()
                                .universityId(university.getId())
                                .name(university.getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
