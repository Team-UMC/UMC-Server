package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCreateResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.mapper.BoardImageMapper;
import com.umc.networkingService.domain.board.mapper.BoardMapper;
import com.umc.networkingService.domain.board.repository.BoardImageRepository;
import com.umc.networkingService.domain.board.repository.BoardRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final S3FileComponent s3FileComponent;
    @Override
    @Transactional
    public BoardCreateResponse createBoard(Member member, BoardCreateRequest request, List<MultipartFile> files) {
        //연합, 지부, 학교 타입
        HostType hostType = request.getHostType();
        BoardType boardType = request.getBoardType();

        //현재 기수
        Semester nowSemester = Semester.findActiveSemester();

        //허용 기수 (기본 모든 기수에게 허용)
        List<Semester> semesterPermission = Arrays.stream(Semester.values()).toList();

        //boardType에 따라 권한 Check
        switch (boardType) {
            case OB -> checkPermissionForOBBoard(member,nowSemester);
            case NOTICE -> checkPermissionForNoticeBoard(member, hostType);
            case WORKBOOK -> {
                checkPermissionForWorkbookBoard(member, hostType);
                //워크북의 경우 현재 기수만 볼 수 있도록 허용
                semesterPermission = List.of(nowSemester);
            }
        }

        Board board = boardRepository.save(BoardMapper.toEntity(member,
                request.getTitle(),
                request.getContent(),
                request.getHostType(),
                request.getBoardType(),
                semesterPermission));

        for(MultipartFile file: files)
            boardImageRepository.save(BoardImageMapper.toEntity(board,s3FileComponent.uploadFile("Board", file)));

        return new BoardCreateResponse(board.getId());
    }

    //OB 게시판 권한 확인 함수
    public void checkPermissionForOBBoard(Member member,Semester nowSemester) {
        // OB -> Semester의 isActive가 활성화되지 않은 사용자만 가능
        if (member.getSemester().contains(nowSemester))
            throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
    }

    //공지사항 게시판 권한 확인 함수
    public void checkPermissionForNoticeBoard(Member member, HostType hostType) {
        //멤버의 역할
        Role memberRole = member.getRole();
        //Center 권한이 있는 멤버의 역할
        List<Role> centerStaffRoles = Role.centerStaffRoles();
        //Branch 권한이 있는 멤버의 역할
        Role branchStaffRole = Role.branchStaffRole();
        //Campus 권한이 있는 멤버의 역할
        List<Role> campusStaffRoles  = Role.campusStaffRoles();

        //공지사항 권한 CHECK
        switch (hostType) {
            case CENTER:
                //CENTER && NOTICE -> ROLE이 TOTAL_STAFF OR CENTER_STAFF 인 사람
                if (!centerStaffRoles.contains(memberRole))
                    throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
                break;
            case BRANCH:
                //BRANCH && NOTICE -> ROLE이 BRANCH_STAFF인 사람
                if (branchStaffRole != memberRole)
                    throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
                //BRANCH 나머지 -> 해당 지부 사람들 모두
                break;
            case CAMPUS:
                //CAMPUS && NOTICE -> ROLE이 CAMPUS_STAFF, STAFF인 사람
                if (!campusStaffRoles.contains(memberRole))
                    throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
                //CAMPUS 나머지 -> 해당 campus 사람들 모두
                break;

        }
    }

    //workbook 게시판 권한 확인 함수
    public void checkPermissionForWorkbookBoard(Member member, HostType hostType) {

    //멤버의 역할
    Role memberRole = member.getRole();
    //Campus 권한이 있는 멤버의 역할
    List<Role> campusStaffRoles  = Role.campusStaffRoles();

    //hostType이 CAMPUS가 아닐 경우 금지된 요청
    if(hostType != HostType.CAMPUS)
        throw new RestApiException(ErrorCode.BAD_REQUEST_BOARD);

    //CAMPUS && WORKBOOK -> ROLE이 CAMPUS_STAFF, STAFF인 사람만
    if (!campusStaffRoles.contains(memberRole))
        throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
    }

}
