package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.dto.request.BoardRequest;
import com.umc.networkingService.domain.board.dto.response.BoardResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.umc.networkingService.domain.board.dto.request.BoardRequest.*;
import static com.umc.networkingService.domain.board.dto.response.BoardResponse.*;

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
    public PinnedNotices showPinnedNotices(Member loginMember, HostType hostType) {
        Member member = memberService.loadEntity(loginMember.getId());

        List<Board> pinnedNotices = (hostType == null) ?
                boardRepository.findPinnedNoticesByMember(member) :
                boardRepository.findPinnedNoticesByMemberAndHostType(member, hostType);

        return boardMapper.toPinnedNotices(
                pinnedNotices.stream().map(notice -> boardMapper.toPinnedNotice(notice,
                        boardMapper.toWriterInfo(notice.getWriter()),
                        boardFileService.findThumbnailImage(notice))).toList()
        );
    }

    @Override
    public BoardPageInfos<BoardPageElement> showBoards(Member loginMember, HostType hostType, BoardType boardType, Pageable pageable) {

        checkBadRequest(hostType, boardType);

        Member member = memberService.loadEntity(loginMember.getId());

        Page<Board> boards = boardRepository.findAllBoards(member, hostType, boardType, pageable);

        return boardMapper.toBoardPageInfos(boards, boards.map(board ->
                boardMapper.toBoardPageElement(board,
                        boardMapper.toWriterInfo(board.getWriter()),
                        boardFileService.findThumbnailImage(board))).stream().toList());
    }

    @Override
    public BoardPageInfos<BoardSearchPageElement> searchBoard(Member loginMember, String keyword, Pageable pageable) {

        Member member = memberService.loadEntity(loginMember.getId());

        Page<Board> searchBoards = boardRepository.findKeywordBoards(member, keyword, pageable);

        return boardMapper.toBoardPageInfos(searchBoards, searchBoards.map(board ->
                boardMapper.toBoardSearchPageElement(board,
                        boardMapper.toWriterInfo(board.getWriter()),
                        boardFileService.findThumbnailImage(board))).stream().toList());
    }

    @Override
    @Transactional
    public BoardDetail showBoardDetail(Member loginMember, UUID boardId) {

        Member member = memberService.loadEntity(loginMember.getId());
        Board board = loadEntity(boardId);

        //게시글 열람 권한 check
        checkReadPermission(member, board);

        //좋아요 여부 확인
        boolean isLike = boardHeartRepository.findByMemberAndBoard(member, board)
                .map(BoardHeart::isChecked)
                .orElse(false);

        //본인글인지 확인
        boolean isMine = checkWriter(board.getWriter(), member);

        //조회수 증가
        board.increaseHitCount();

        //해당 게시글의 모든 첨부파일 url
        List<String> boardFiles = boardFileService.findBoardFiles(board).stream()
                .map(BoardFile::getUrl).toList();

        return boardMapper.toBoardDetail(board, boardFiles, boardMapper.toDetailWriterInfo(board.getWriter()), isLike, isMine);
    }

    @Override
    @Transactional
    public BoardId toggleBoardLike(Member member, UUID boardId) {
        Board board = loadEntity(boardId);

        //boardHeart에 없다면 새로 저장
        BoardHeart boardHeart = boardHeartRepository.findByMemberAndBoard(member, board)
                .orElseGet(() -> {
                    BoardHeart newHeart = boardHeartMapper.toBoardHeartEntity(board, member);
                    return boardHeartRepository.save(newHeart);
                });

        boardHeart.toggleHeart();
        board.setHeartCount(boardHeart.isChecked());

        return new BoardResponse.BoardId(boardId);
    }


    @Override
    public BoardPageInfos<MyBoardPageElement> showBoardsByMember(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable) {
        Page<Board> boards = (hostType != null && boardType != null) ?
                boardRepository.findBoardsByWriterForWeb(member, hostType, boardType, keyword, pageable) :
                boardRepository.findBoardsByWriterForApp(member, keyword, pageable);

        return boardMapper.toBoardPageInfos(boards, boards.map(boardMapper::toMyBoardPageElement).stream().toList());
    }


    @Override
    public BoardPageInfos<MyBoardPageElement> showBoardsByMemberHeart(Member member, HostType hostType, BoardType boardType, String keyword, Pageable pageable) {
        Page<Board> boards = (hostType != null && boardType != null) ?
                boardRepository.findBoardsByMemberHeartForWeb(member, hostType, boardType, keyword, pageable) :
                boardRepository.findBoardsByMemberHeartForApp(member, keyword, pageable);

        return boardMapper.toBoardPageInfos(boards, boards.map(boardMapper::toMyBoardPageElement).stream().toList());
    }


    @Override
    @Transactional
    public BoardId createBoard(Member member, BoardCreateRequest request, List<MultipartFile> files) {
        //연합, 지부, 학교 타입
        HostType hostType = HostType.valueOf(request.getHostType());
        BoardType boardType = BoardType.valueOf(request.getBoardType());

        //boardType과 HostType에 따라 금지된 요청, 권한 판단
        checkBadRequest(hostType, boardType);
        List<Semester> semesterPermission = checkWritePermission(member, hostType, boardType);

        Board board = boardRepository.save(boardMapper.toEntity(member, request, semesterPermission));
        if (files != null)
            boardFileService.uploadBoardFiles(board, files);

        return new BoardResponse.BoardId(board.getId());
    }


    @Override
    @Transactional
    public BoardId updateBoard(Member loginMember, UUID boardId, BoardUpdateRequest request, List<MultipartFile> files) {
        Member member = memberService.loadEntity(loginMember.getId());
        Board board = loadEntity(boardId);

        //연합, 지부, 학교 타입
        HostType hostType = HostType.valueOf(request.getHostType());
        BoardType boardType = BoardType.valueOf(request.getBoardType());

        //boardType과 HostType에 따라 금지된 요청, 권한 판단
        checkBadRequest(hostType, boardType);
        List<Semester> semesterPermission = checkWritePermission(member, hostType, boardType);

        //현재 로그인한 member와 writer가 같지 않으면 수정 권한 없음
        if (!checkWriter(board.getWriter(), member))
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);

        board.update(request, semesterPermission);
        boardFileService.updateBoardFiles(board, files);

        return new BoardResponse.BoardId(board.getId());
    }


    @Override
    @Transactional
    public BoardId deleteBoard(Member loginMember, UUID boardId) {
        Member member = memberService.loadEntity(loginMember.getId());
        Board board = loadEntity(boardId);
        Member writer = board.getWriter();

        //현재 로그인한 member와 writer가 같지 않고, 로그인 한 멤버보다 상위 운영진이 아니라면 예외 반환
        if (!checkWriter(writer, member)) {
            if (!checkHighStaff(writer, member)) {
                throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);
            }
        }

        boardFileService.deleteBoardFiles(board);
        board.delete();

        return new BoardResponse.BoardId(board.getId());

    }

    @Override
    public Boolean existsByBoardTypeAndHostType(BoardType boardType, HostType hostType) {
        return boardRepository.existsByBoardTypeAndHostType(boardType, hostType);
    }

    //게시글 열람 시 권한 check
    @Override
    public void checkReadPermission(Member member, Board board) {
        //if hostType == CAMPUS -> board writer의 university와 로그인 member의 university 일치한지 확인
        //if hostType == BRANCH -> board writer의 branch와 로그인 member의 branch 일치한지 확인
        boolean hasPermission = true;

        switch (board.getHostType()) {
            case CAMPUS ->
                    hasPermission = board.getWriter().getUniversity().getId().equals(member.getUniversity().getId());
            case BRANCH ->
                    hasPermission = board.getWriter().getBranch().getId().equals(member.getBranch().getId());
        }

        if (!hasPermission || !checkSemesterPermission(member,board)) {
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);
        }

    }

    //게시글 열람 시 semester 권한 check
    private static boolean checkSemesterPermission(Member member, Board board) {
        List<Semester> memberSemesters = member.getSemesters();
        List<Semester> boardSemesterPermissions = board.getSemesterPermission();

        //memberSemester의 어떤 기수도 허용기수에 포함되지 않으면 false
        return memberSemesters.stream()
                .anyMatch(boardSemesterPermissions::contains);
    }

    //쓰기 권한 check
    private List<Semester> checkWritePermission(Member member, HostType hostType, BoardType boardType) {
        //현재 기수
        Semester nowSemester = Semester.findActiveSemester();

        //허용 기수 (기본 모든 기수에게 허용)
        List<Semester> permissionSemesters = Arrays.stream(Semester.values()).toList();

        //boardType에 따라 권한 Check
        switch (boardType) {
            case OB -> checkInactiveMember(member, nowSemester);
            case NOTICE -> checkNoticePermissionStaff(member, hostType);
            case WORKBOOK -> {
                checkStaff(member);
                //워크북의 경우 현재 기수만 볼 수 있도록 허용
                permissionSemesters = List.of(nowSemester);
            }
        }
        return permissionSemesters;
    }


    //멤버가 현재 기수일 경우 예외 반환
    // OB -> Semester의 isActive가 활성화되지 않은 사용자만 가능
    private void checkInactiveMember(Member member, Semester nowSemester) {

        if (member.getRecentSemester().equals(nowSemester))
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);
    }


    //NOTICE -> hostType 권한보다 멤버 권한이 높거나 같지 않을 경우 예외 반환
    private void checkNoticePermissionStaff(Member member, HostType hostType) {

        //HostType priority와 Member Role priority를 비교하여 권한 CHECK
        if (member.getRole().getPriority() >= hostType.getPriority())
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);
    }


    //일반 멤버일 경우 예외 발생
    //CAMPUS && WORKBOOK -> 일반 MEMBER는 작성 불가
    private void checkStaff(Member member) {

        if (member.getRole().getPriority() >= Role.MEMBER.getPriority())
            throw new RestApiException(BoardErrorCode.NO_AUTHORIZATION_BOARD);
    }

    private void checkBadRequest(HostType hostType, BoardType boardType) {
        //boardType: workbook, hostType: CAMPUS 가 아닐 경우 금지된 요청
        if (boardType == BoardType.WORKBOOK && hostType != HostType.CAMPUS)
            throw new RestApiException(BoardErrorCode.BAD_REQUEST_BOARD);
        //boardType: OB, hostType: Branch일 경우 금지된 요청
        if (boardType == BoardType.OB && hostType == HostType.BRANCH)
            throw new RestApiException(BoardErrorCode.BAD_REQUEST_BOARD);
    }


    @Override
    public boolean checkWriter(Member writer, Member member) {
        return writer.getId() == member.getId();
    }


    @Override
    public boolean checkHighStaff(Member writer, Member member) {
        return member.getRole().getPriority() < writer.getRole().getPriority();
    }


    @Override
    public Board loadEntity(UUID id) {
        return boardRepository.findById(id).orElseThrow(() -> new RestApiException(BoardErrorCode.EMPTY_BOARD));
    }
}
