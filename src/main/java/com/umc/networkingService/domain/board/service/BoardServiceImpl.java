package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.dto.request.BoardRequest;
import com.umc.networkingService.domain.board.dto.response.BoardResponse;
import com.umc.networkingService.domain.board.dto.response.MyBoardResponse;
import com.umc.networkingService.domain.board.entity.*;
import com.umc.networkingService.domain.board.mapper.BoardHeartMapper;
import com.umc.networkingService.domain.board.mapper.BoardMapper;
import com.umc.networkingService.domain.board.repository.BoardHeartRepository;
import com.umc.networkingService.domain.board.repository.BoardRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.BoardErrorCode;
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
    private final MemberService memberService;


    @Override
    public BoardResponse.PinnedNotices showPinnedNotices(Member loginMember) {

        Member member = memberService.loadEntity(loginMember.getId());

        return boardMapper.toPinnedNotices(boardRepository.findNoticesByMember(member));

    }
    @Override
    public BoardResponse.BoardPageInfos showBoards(Member loginMember, HostType hostType, BoardType boardType, Pageable pageable) {

        Member member = memberService.loadEntity(loginMember.getId());
        checkBadRequest(hostType,boardType);

        return boardMapper.toBoardPageInfos(boardRepository.findAllBoards(member, hostType, boardType, pageable));
    }


    @Override
    public BoardResponse.BoardSearchPageInfos searchBoard(Member loginMember, String keyword, Pageable pageable) {

        Member member = memberService.loadEntity(loginMember.getId());

        return boardMapper.toBoardSearchPageInfos(boardRepository.findKeywordBoards(member, keyword, pageable));

    }

    @Override
    @Transactional
    public BoardResponse.BoardDetail showBoardDetail(Member loginMember, UUID boardId) {

        Member member = memberService.loadEntity(loginMember.getId());
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

        return boardMapper.toBoardDetail(board, boardFiles, isLike);

    }


    @Override
    @Transactional
    public BoardResponse.BoardId toggleBoardLike(Member member, UUID boardId) {
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

        return new BoardResponse.BoardId(boardId);
    }


    @Override
    public MyBoardResponse.MyBoardPageInfos showBoardsByMemberForApp(Member member, String keyword, Pageable pageable) {
        return boardMapper.toMyBoardPageInfos(boardRepository.findBoardsByWriterForApp(member, keyword, pageable));
    }

    @Override
    public MyBoardResponse.MyBoardPageInfos showBoardsByMemberForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable) {
        return boardMapper.toMyBoardPageInfos(boardRepository.findBoardsByWriterForWeb(member, hostType, boardType, keyword, pageable));
    }

    @Override
    public MyBoardResponse.MyBoardPageInfos showBoardsByMemberHeartForApp(Member member, String keyword, Pageable pageable) {
        return boardMapper.toMyBoardPageInfos(boardRepository.findBoardsByMemberHeartForApp(member, keyword, pageable));
    }

    @Override
    public MyBoardResponse.MyBoardPageInfos showBoardsByMemberHeartForWeb(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable) {
        return boardMapper.toMyBoardPageInfos(boardRepository.findBoardsByMemberHeartForWeb(member, hostType, boardType, keyword, pageable));
    }

    @Override
    public Boolean existsByBoardTypeAndHostType(BoardType boardType, HostType hostType) {
        return boardRepository.existsByBoardTypeAndHostType(boardType, hostType);
    }


    @Override
    @Transactional
    public BoardResponse.BoardId createBoard(Member member, BoardRequest.BoardCreateRequest request, List<MultipartFile> files) {
        //연합, 지부, 학교 타입
        HostType hostType = HostType.valueOf(request.getHostType());
        BoardType boardType = BoardType.valueOf(request.getBoardType());

        //boardType과 HostType에 따라 금지된 요청, 권한 판단
        checkBadRequest(hostType,boardType);
        List<Semester> semesterPermission = checkPermission(member, hostType, boardType);


        Board board = boardRepository.save(boardMapper.toEntity(member, request, semesterPermission));
        if (files != null)
            boardFileService.uploadBoardFiles(board, files);

        return new BoardResponse.BoardId(board.getId());
    }

    @Override
    @Transactional
    public BoardResponse.BoardId updateBoard(Member member, UUID boardId, BoardRequest.BoardUpdateRequest request, List<MultipartFile> files) {
        Board board = loadEntity(boardId);

        //연합, 지부, 학교 타입
        HostType hostType = HostType.valueOf(request.getHostType());
        BoardType boardType = BoardType.valueOf(request.getBoardType());

        //boardType과 HostType에 따라 금지된 요청, 권한 판단
        checkBadRequest(hostType,boardType);
        List<Semester> semesterPermission = checkPermission(member, hostType, boardType);

        //현재 로그인한 member와 writer가 같지 않으면 수정 권한 없음
        if (!board.getWriter().getId().equals(member.getId()))
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);

        board.update(request, semesterPermission);
        boardFileService.updateBoardFiles(board, files);

        return new BoardResponse.BoardId(board.getId());
    }

    @Override
    @Transactional
    public BoardResponse.BoardId deleteBoard(Member member, UUID boardId) {
        Board board = loadEntity(boardId);

        //현재 로그인한 member와 writer가 같지 않고, 로그인한 멤버가 운영진이 아니라면 삭제 불가
        if (!board.getWriter().getId().equals(member.getId())) {
            if (member.getRole().getPriority() >= Role.MEMBER.getPriority()) {
                throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);
            }
        }
        boardFileService.deleteBoardFiles(board);
        board.delete();

        return new BoardResponse.BoardId(board.getId());

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
                checkPermissionForWorkbookBoard(member);
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
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);
    }

    //공지사항 게시판 권한 확인 함수
    public void checkPermissionForNoticeBoard(Member member, HostType hostType) {

        //HostType priority와 Member Role priority를 비교하여 권한 CHECK
        if (member.getRole().getPriority() >= hostType.getPriority())
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);
    }

    //workbook 게시판 권한 확인 함수
    public void checkPermissionForWorkbookBoard(Member member) {

        //CAMPUS && WORKBOOK -> 일반 MEMBER는 작성 불가
        if (member.getRole().getPriority() >= Role.MEMBER.getPriority())
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);
    }

    //게시글 열람시 semester 권한 check
    private static void checkSemesterPermission(Member member, Board board) {
        List<Semester> memberSemesters = member.getSemesters();
        List<Semester> boardSemesterPermissions = board.getSemesterPermission();

        if (!memberSemesters.stream()
                .anyMatch(boardSemesterPermissions::contains))
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);

    }

    public void checkBadRequest(HostType hostType, BoardType boardType) {
        //운영진 공지사항 목록 조회 제외하고는 HostType ALL 불가능
        if (hostType.equals(HostType.ALL))
            throw new RestApiException(BoardErrorCode.BAD_REQUEST_BOARD);
        //boardType: workbook, hostType: CAMPUS 가 아닐 경우 금지된 요청
        if (boardType == BoardType.WORKBOOK && hostType != HostType.CAMPUS)
            throw new RestApiException(BoardErrorCode.BAD_REQUEST_BOARD);
        //boardType: OB, hostType: Branch일 경우 금지된 요청
        if (boardType == BoardType.OB && hostType == HostType.BRANCH)
            throw new RestApiException(BoardErrorCode.BAD_REQUEST_BOARD);
    }


    @Override
    public Board loadEntity(UUID id) {
        return boardRepository.findById(id).orElseThrow(() -> new RestApiException(BoardErrorCode.EMPTY_BOARD));
    }
}
