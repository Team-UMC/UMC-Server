package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardDetailResponse;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.entity.*;
import com.umc.networkingService.domain.board.mapper.BoardHeartMapper;
import com.umc.networkingService.domain.board.mapper.BoardMapper;
import com.umc.networkingService.domain.board.repository.BoardHeartRepository;
import com.umc.networkingService.domain.board.repository.BoardRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileService boardFileService;
    private final BoardMapper boardMapper;
    private final BoardHeartRepository boardHeartRepository;
    private final BoardHeartMapper boardHeartMapper;


    @Override
    public BoardPagingResponse showBoards(Member member, HostType hostType, BoardType boardType, Pageable pageable) {
        return boardMapper.toBoardPagingResponse(boardRepository.findAllBoards(member, hostType, boardType, pageable));
    }

    @Override
    public BoardDetailResponse showBoardDetail(Member member, UUID boardId) {

        Board board = loadEntity(boardId);

        //조회수 증가
        board.increaseHitCount();

        //해당 게시글의 모든 첨부파일 url
        List<String> boardFiles = boardFileService.findBoardFiles(board).stream()
                .map(BoardFile::getUrl).toList();

        return boardMapper.toBoardDetailResponse(board, boardFiles);

    }

    @Override
    public BoardPagingResponse searchBoard(Member member, String keyword, Pageable pageable) {

        return boardMapper.toBoardPagingResponse(boardRepository.findKeywordBoards(member, keyword, pageable));

    }

    @Override
    @Transactional
    public BoardIdResponse toggleBoardLike(Member member, UUID boardId) {
        Board board = loadEntity(boardId);

        //boardHeart에 없다면 새로 저장
        BoardHeart boardHeart = boardHeartRepository.findByMemberAndBoard(member, board)
                .orElseGet(() -> {
                    BoardHeart newHeart = boardHeartMapper.toBoardHeartEntity(board, member);
                    boardHeartRepository.save(newHeart);
                    board.addBoardHeart(newHeart);
                    return newHeart;
                });

        boardHeart.toggleHeart();
        board.setHeartCount(boardHeart.isChecked());

        return new BoardIdResponse(boardId);
    }

    @Override
    @Transactional
    public BoardIdResponse createBoard(Member member, BoardCreateRequest request, List<MultipartFile> files) {
        //연합, 지부, 학교 타입
        HostType hostType = request.getHostType();
        BoardType boardType = request.getBoardType();

        //boardType과 HostType에 따라 권한 판단
        List<Semester> semesterPermission = checkPermission(member, hostType, boardType);

        Board board = boardRepository.save(boardMapper.toEntity(member,
                request.getTitle(),
                request.getContent(),
                request.getHostType(),
                request.getBoardType(),
                semesterPermission));

        if (files != null)
            boardFileService.uploadBoardFiles(board, files);

        return new BoardIdResponse(board.getId());
    }

    @Override
    @Transactional
    public BoardIdResponse updateBoard(Member member, UUID boardId, BoardUpdateRequest request, List<MultipartFile> files) {

        Board board = loadEntity(boardId);

        //연합, 지부, 학교 타입
        HostType hostType = request.getHostType();
        BoardType boardType = request.getBoardType();

        //boardType과 HostType에 따라 권한 판단
        List<Semester> semesterPermission = checkPermission(member, hostType, boardType);

        board.update(request.getHostType(),
                request.getBoardType(),
                request.getTitle(),
                request.getContent(),
                semesterPermission);

        boardFileService.updateBoardFiles(board, files);

        return new BoardIdResponse(board.getId());
    }

    @Override
    @Transactional
    public BoardIdResponse deleteBoard(Member member, UUID boardId) {
        Board board = loadEntity(boardId);

        boardFileService.deleteBoardFiles(board);
        board.delete();

        return new BoardIdResponse(board.getId());

    }


    public List<Semester> checkPermission(Member member, HostType hostType, BoardType boardType) {
        //현재 기수
        Semester nowSemester = Semester.findActiveSemester();

        //허용 기수 (기본 모든 기수에게 허용)
        List<Semester> permissionSemesters = Arrays.stream(Semester.values()).toList();

        //boardType에 따라 권한 Check
        switch (boardType) {
            case OB -> checkPermissionForOBBoard(member, nowSemester);
            case NOTICE -> checkPermissionForNoticeBoard(member, hostType);
            case WORKBOOK -> {
                checkPermissionForWorkbookBoard(member, hostType);
                //워크북의 경우 현재 기수만 볼 수 있도록 허용
                permissionSemesters = List.of(nowSemester);
            }
        }
        return permissionSemesters;
    }


    //OB 게시판 권한 확인 함수
    public void checkPermissionForOBBoard(Member member, Semester nowSemester) {
        // OB -> Semester의 isActive가 활성화되지 않은 사용자만 가능
        if (member.getRecentSemester().equals(nowSemester))
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
        List<Role> campusStaffRoles = Role.campusStaffRoles();

        // 공지사항 권한 CHECK
        if (hostType == HostType.CENTER) {
            // CENTER && NOTICE -> ROLE이 TOTAL_STAFF OR CENTER_STAFF 인 사람
            if (!centerStaffRoles.contains(memberRole)) {
                throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
            }
        } else if (hostType == HostType.BRANCH) {
            // BRANCH && NOTICE -> ROLE이 BRANCH_STAFF인 사람
            if (branchStaffRole != memberRole) {
                throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
            }
            // BRANCH 나머지 -> 해당 지부 사람들 모두
        } else if (hostType == HostType.CAMPUS) {
            // CAMPUS && NOTICE -> ROLE이 CAMPUS_STAFF, STAFF인 사람
            if (!campusStaffRoles.contains(memberRole)) {
                throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
            }
            // CAMPUS 나머지 -> 해당 campus 사람들 모두
        }

    }

    //workbook 게시판 권한 확인 함수
    public void checkPermissionForWorkbookBoard(Member member, HostType hostType) {

        //멤버의 역할
        Role memberRole = member.getRole();
        //Campus 권한이 있는 멤버의 역할
        List<Role> campusStaffRoles = Role.campusStaffRoles();

        //hostType이 CAMPUS가 아닐 경우 금지된 요청
        if (hostType != HostType.CAMPUS)
            throw new RestApiException(ErrorCode.BAD_REQUEST_BOARD);

        //CAMPUS && WORKBOOK -> ROLE이 CAMPUS_STAFF, STAFF인 사람만
        if (!campusStaffRoles.contains(memberRole))
            throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
    }


    @Override
    public Board loadEntity(UUID id) {
        return boardRepository.findById(id).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_BOARD));
    }
}
