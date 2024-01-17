package com.umc.networkingService.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /*
     * 에러
     */
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON402", "Validation Error입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 정보를 찾을 수 없습니다."),

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "예외처리 테스트입니다."),

    // Member
    EMPTY_MEMBER(HttpStatus.CONFLICT, "MEMBER001", "존재하지 않는 사용자입니다."),

    // Auth
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "AUTH001", "JWT가 없습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "AUTH002", "유효하지 않은 JWT입니다."),
    EXPIRED_MEMBER_JWT(HttpStatus.UNAUTHORIZED, "AUTH003", "만료된 JWT입니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "AUTH004", "지원하지 않는 JWT입니다."),
    INVALID_ID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH005", "유효하지 않은 ID TOKEN입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "AUTH006", "유효하지 않은 ACCESS TOKEN입니다."),
    FAILED_SOCIAL_LOGIN(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH007", "소셜 로그인에 실패하였습니다."),

    //page
    PAGE_NOT_VALID(HttpStatus.BAD_REQUEST, "PAGE001", "페이지 값이 유효하지 않습니다."),

    //Branch
    BRANCH_NOT_FOUND(HttpStatus.CONFLICT, "BRANCH001", "존재하지 않는 Branch입니다."),
    BRANCH_SEMESTER_EMPTY(HttpStatus.CONFLICT, "BRANCH004", "Branch 기수가 비어있거나, 잘못된 값임니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
