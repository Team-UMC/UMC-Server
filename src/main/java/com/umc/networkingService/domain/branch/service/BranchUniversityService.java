package com.umc.networkingService.domain.branch.service;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.enums.Semester;

import java.util.List;

public interface BranchUniversityService {
    Branch findBranchByUniversityAndSemester(University university, Semester LastSemester);

    Branch findBranchByUniversity(University university);
    List<University> findUniversitiesByBranch(Branch branch);

}
