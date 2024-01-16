package com.umc.networkingService.domain.branch.execption;

import ch.qos.logback.core.status.ErrorStatus;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;

public class BranchHandler extends RestApiException {

    public BranchHandler(ErrorCode error) {
        super(error);
    }
}