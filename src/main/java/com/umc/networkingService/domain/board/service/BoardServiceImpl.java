package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.request.BoardUpdateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardDetailResponse;
import com.umc.networkingService.domain.board.dto.response.BoardIdResponse;
import com.umc.networkingService.domain.board.dto.response.BoardPagingResponse;
import com.umc.networkingService.domain.board.dto.response.BoardSearchPagingResponse;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardPageResponse;
import com.umc.networkingService.domain.board.dto.response.member.MyBoardPagingResponse;
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
import java.util.Optional;
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
    public BoardSearchPagingResponse searchBoard(Member member, String keyword, Pageable pageable) {

        return boardMapper.toBoardSearchPagingResponse(boardRepository.findKeywordBoards(member, keyword, pageable));

    }

    @Override
    public BoardDetailResponse showBoardDetail(Member member, UUID boardId) {

        Board board = loadEntity(boardId);

        //게시글을 열람할 권한이 있는지 check
        checkSemesterPermission(member, board);

        //좋아요 여부 확인
        boolean isLike = false;
        Optional<BoardHeart> boardHeart = boardHeartRepository.findByMemberAndBoard(member, board);
        if (boardHeart.isPresent())
            isLike = boardHeart.get().isChecked();

        //조회수 증가
        board.increaseHitCount();

        //해당 게시글의 모든 첨부파일 url
        List<String> boardFiles = boardFileService.findBoardFiles(board).stream()
                .map(BoardFile::getUrl).toList();

        return boardMapper.toBoardDetailResponse(board, boardFiles, isLike);

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
                    return newHeart;
                });

        boardHeart.toggleHeart();
        board.setHeartCount(boardHeart.isChecked());

        return new BoardIdResponse(boardId);
    }


    @Override
    public MyBoardPagingResponse showBoardsByMember(Member member, String keyword, Pageable pageable) {
        return boardMapper.toMyBoardPagingResponse(boardRepository.findBoardsByWriter(member, keyword, pageable));
    }

    @Override
    public MyBoardPagingResponse showBoardsByMemberHearts(Member member, String keyword, Pageable pageable) {
        return boardMapper.toMyBoardPagingResponse(boardRepository.findBoardsByMemberHearts(member, keyword, pageable));
    }



    @Override
    @Transactional
    public BoardIdResponse createBoard(Member member, BoardCreateRequest request, List<MultipartFile> files) {
        //연합, 지부, 학교 타입
        HostType hostType = request.getHostType();
        BoardType boardType = request.getBoardType();

        //boardType과 HostType에 따라 권한 판단
        List<Semester> semesterPermission = checkPermission(member, hostType, boardType);

        Board board = boardRepository.save(boardMapper.toEntity(member, request, semesterPermission));

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

        //현재 로그인한 member와 writer가 같지 않으면 수정 권한 없음
        if (!board.getWriter().equals(member))
            throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);

        board.update(request, semesterPermission);

        boardFileService.updateBoardFiles(board, files);

        return new BoardIdResponse(board.getId());
    }

    @Override
    @Transactional
    public BoardIdResponse deleteBoard(Member member, UUID boardId) {
        Board board = loadEntity(boardId);

        //현재 로그인한 member와 writer가 같지 않고, 로그인한 멤버가 운영진이 아니라면 삭제 불가
        if (!board.getWriter().equals(member)) {
            // staff 역할을 가진 경우에는 삭제 권한이 있음
            if (member.getRole().getPriority() == Role.MEMBER.getPriority()) {
                throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
            }
        }
        boardFileService.deleteBoardFiles(board);
        board.delete();

        return new BoardIdResponse(board.getId());

    }


    //게시글 작성, 수정 시 권한 Check
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

        //HostType priority와 Member Role priority를 비교하여 권한 CHECK
        if (member.getRole().getPriority() >= hostType.getPriority())
            throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);

    }

    //workbook 게시판 권한 확인 함수
    public void checkPermissionForWorkbookBoard(Member member, HostType hostType) {

        //hostType이 CAMPUS가 아닐 경우 금지된 요청
        if (hostType != HostType.CAMPUS)
            throw new RestApiException(ErrorCode.BAD_REQUEST_BOARD);

        //CAMPUS && WORKBOOK -> ROLE이 CAMPUS_STAFF인 사람만
        if (member.getRole().getPriority() != Role.CAMPUS_STAFF.getPriority())
            throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
    }

    //게시글 열람시 semester 권한 check
    private static void checkSemesterPermission(Member member, Board board) {
        List<Semester> memberSemesters = member.getSemesters();
        List<Semester> boardSemesterPermissions = board.getSemesterPermission();

        if(!memberSemesters.stream()
                .anyMatch(boardSemesterPermissions::contains))
            throw new RestApiException(ErrorCode.FORBIDDEN_MEMBER);
    }

    @Override
    public Board loadEntity(UUID id) {
        return boardRepository.findById(id).orElseThrow(() -> new RestApiException(ErrorCode.EMPTY_BOARD));
    }
}
