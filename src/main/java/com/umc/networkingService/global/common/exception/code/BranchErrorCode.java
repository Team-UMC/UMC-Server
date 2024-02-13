package com.umc.networkingService.global.common.exception.code;

import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BranchErrorCode implements ErrorCodeInterface {

    //Branch
    BRANCH_NOT_FOUND(HttpStatus.CONFLICT, "BRANCH001", "존재하지 않는 Branch입니다."),
    BRANCH_SEMESTER_EMPTY(HttpStatus.CONFLICT, "BRANCH004", "Branch 기수가 비어있거나, 잘못된 값임니다."),
    BRANCH_NAME_EMPTY(HttpStatus.CONFLICT, "BRANCH002", "Branch 이름이 없습니다."),
    BRANCH_DESCRIPTION_EMPTY(HttpStatus.CONFLICT, "BRANCH003", "Branch 설명이 없습니다."),
    BRANCH_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"BRANCH005", "Branch 저장에 실패 했습니다"),

    //BranchUniversity
    BRANCH_UNIVERSITY_NOT_FOUND(HttpStatus.CONFLICT, "BRANCH_UNIVERSITY001", "존재하지 않는 BranchUniversity입니다."),
    BRANCH_UNIVERSITY_ALREADY_EXIST(HttpStatus.CONFLICT, "BRANCH_UNIVERSITY002", "이미 존재하는 BranchUniversity입니다."),

    SEMESTER_NOT_VALID(HttpStatus.BAD_REQUEST, "SEMESTER001", "Semester 값이 유효하지 않습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }

}
