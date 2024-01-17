package com.umc.networkingService.domain.branch.execption;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;

public class BranchUniversityHandler extends RestApiException {

    public BranchUniversityHandler(ErrorCode error) {
        super(error);
    }
}