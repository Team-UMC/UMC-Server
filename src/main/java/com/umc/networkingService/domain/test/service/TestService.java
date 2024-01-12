package com.umc.networkingService.domain.test.service;

import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
    public void CheckFlag(Integer flag) {
        if (flag == 1)
            throw new RestApiException(ErrorCode.TEMP_EXCEPTION);
    }
}
