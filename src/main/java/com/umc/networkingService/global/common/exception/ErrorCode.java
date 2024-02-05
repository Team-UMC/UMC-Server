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

    _METHOD_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "COMMON405", "Argument Type이 올바르지 않습니다."),

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "예외처리 테스트입니다."),

    // Member
    EMPTY_MEMBER(HttpStatus.CONFLICT, "MEMBER001", "존재하지 않는 사용자입니다."),
    UNAUTHORIZED_UPDATE_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER002", "상위 운영진의 직책을 수정할 수 없습니다."),
    UNAUTHORIZED_UPDATE_CENTER_POSITION(HttpStatus.BAD_REQUEST, "MEMBER003", "지부, 학교 운영진은 중앙 직책을 부여할 수 없습니다."),
    EMPTY_MEMBER_UNIVERSITY(HttpStatus.CONFLICT, "MEMBER004", "소속 대학교가 존재하지 않는 사용자입니다."),
    UNAUTHENTICATED_GITHUB(HttpStatus.BAD_REQUEST, "MEMBER005", "깃허브 연동이 완료되지 않은 사용자입니다."),
    INVALID_MEMBER_KEYWORD(HttpStatus.BAD_REQUEST, "MEMBER006", "검색어 양식[닉네임/이름]에 맞추어 작성해주세요. ex) 벡스/김준석"),
    NO_PERMISSION_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER006", "권한이 없는 사용자입니다."),
    NO_SEMESTER_PARTS(HttpStatus.BAD_REQUEST, "MEMBER007", "참여한 기수와 파트가 존재하지 않는 사용자입니다."),

    // Friend
    ALREADY_FRIEND_RELATION(HttpStatus.BAD_REQUEST, "FRIEND001", "이미 친구 관계인 사용자입니다."),
    NOT_FRIEND_RELATION(HttpStatus.CONFLICT, "FRIEND002", "친구 관계가 아닌 사용자입니다."),

    // SemesterPart
    EMPTY_SEMESTER_PART(HttpStatus.BAD_REQUEST, "PART006", "존재하지 않는 기수의 파트입니다."),

    // Invite
    EXPIRED_INVITE_CODE(HttpStatus.BAD_REQUEST, "INVITE001", "이미 만료된 초대 코드입니다."),
    UNAUTHORIZED_CREATE_INVITE(HttpStatus.BAD_REQUEST, "INVITE002", "해당 역할에 대해서 초대 코드를 생성할 권한이 없습니다."),

    // Auth
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "AUTH001", "JWT가 없습니다."),
    EXPIRED_MEMBER_JWT(HttpStatus.UNAUTHORIZED, "AUTH002", "만료된 JWT입니다."),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "AUTH003", "지원하지 않는 JWT입니다."),
    INVALID_ID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH004", "유효하지 않은 ID TOKEN입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "AUTH005", "유효하지 않은 ACCESS TOKEN입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "AUTH006", "유효하지 않은 REFRESH TOKEN입니다."),
    FAILED_SOCIAL_LOGIN(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH007", "소셜 로그인에 실패하였습니다."),
    FAILED_GITHUB_AUTHENTICATION(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH008", "깃허브 서버와 통신이 실패하였습니다."),


    //page
    PAGE_NOT_VALID(HttpStatus.BAD_REQUEST, "PAGE001", "페이지 값이 유효하지 않습니다."),

    //semester
    SEMESTER_NOT_VALID(HttpStatus.BAD_REQUEST, "SEMESTER001", "Semester 값이 유효하지 않습니다."),

    //Branch
    BRANCH_NOT_FOUND(HttpStatus.CONFLICT, "BRANCH001", "존재하지 않는 Branch입니다."),
    BRANCH_SEMESTER_EMPTY(HttpStatus.CONFLICT, "BRANCH004", "Branch 기수가 비어있거나, 잘못된 값임니다."),
    BRANCH_NAME_EMPTY(HttpStatus.CONFLICT, "BRANCH002", "Branch 이름이 없습니다."),
    BRANCH_DESCRIPTION_EMPTY(HttpStatus.CONFLICT, "BRANCH003", "Branch 설명이 없습니다."),
    BRANCH_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"BRANCH005", "Branch 저장에 실패 했습니다"),

    //BranchUniversity
    BRANCH_UNIVERSITY_NOT_FOUND(HttpStatus.CONFLICT, "BRANCH_UNIVERSITY001", "존재하지 않는 BranchUniversity입니다."),
    BRANCH_UNIVERSITY_ALREADY_EXIST(HttpStatus.CONFLICT, "BRANCH_UNIVERSITY002", "이미 존재하는 BranchUniversity입니다."),

    //University
    UNIVERSITY_NOT_FOUND(HttpStatus.CONFLICT, "UNIVERSITY001", "존재하지 않는 University입니다."),

    // University
    EMPTY_UNIVERSITY(HttpStatus.BAD_REQUEST, "UNIVERSITY001", "존재하지 않는 대학교입니다."),

    // Branch
    EMPTY_BRANCH(HttpStatus.BAD_REQUEST, "BRANCH001", "존재하지 않는 지부입니다."),

    // TodoList
    EMPTY_TODOLIST(HttpStatus.BAD_REQUEST, "TODOLIST001", "존재하지 않은 투두리스트입니다."),
    NO_AUTHORIZATION_TODOLIST(HttpStatus.BAD_REQUEST, "TODOLIST002", "해당 투두리스트에 대한 수정 권한이 없습니다."),

    // Image
    FAILED_UPLOAD_S3_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE001", "이미지 저장에 실패하였습니다."),

    // File
    FAILED_UPLOAD_S3_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "FILE001", "파일 저장에 실패하였습니다."),

    // Schedule
    EMPTY_SCHEDULE(HttpStatus.BAD_REQUEST, "SCHEDULE001", "존재하지 않는 스케줄입니다."),

    //Board
    BAD_REQUEST_BOARD(HttpStatus.BAD_REQUEST,"BOARD001", "금지된 요청입니다."),
    EMPTY_BOARD(HttpStatus.NOT_FOUND, "BOARD002", "게시글을 찾을 수 없습니다."),
    NO_AUTHORIZATION_BOARD(HttpStatus.BAD_REQUEST,"BOARD003", "해당 게시판 API에 대한 권한이 없습니다."),

    //BoardComment
    EMPTY_BOARD_COMMENT(HttpStatus.NOT_FOUND,"COMMENT001","댓글을 찾을 수 없습니다."),
    NO_AUTHORIZATION_BOARD_COMMENT(HttpStatus.BAD_REQUEST,"COMMENT002", "댓글을 작성/수정/삭제 할 권한이 없습니다."),

    // TodayILearned
    EMPTY_TODAYILERARNED(HttpStatus.BAD_REQUEST, "TODAYILEANRED001", "존재하지 않는 TIL입니다."),
    NO_PERMISSION_EMPTY_TODAYILERARNED_MEMBER(HttpStatus.BAD_REQUEST, "TODAYILEANRED002", "해당 TIL에 대해 권한이 없는 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
