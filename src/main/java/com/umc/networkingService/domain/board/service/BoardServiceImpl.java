package com.umc.networkingService.domain.board.service;


import com.umc.networkingService.domain.board.dto.request.BoardCreateRequest;
import com.umc.networkingService.domain.board.dto.response.BoardCreateResponse;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.repository.BoardRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public BoardCreateResponse boardCreate(Member member, BoardCreateRequest request, List<MultipartFile> files) {

        Role memberRole = member.getRole();

        //CENTER && NOTICE -> ROLE이  TOTAL_STAFF OR CENTER_STAFF 인 사람
        //CENTER && OB -> ENUM에 ISACTIVE넣고 아닌 사용자만 가능
        //CENTER 나머지 -> 전체 다 쓸 수 있음

        //BRANCH && NOTICE -> ROLE이 BRANCH_STAFF인 사람
        //BRANCH && OB -> ENUM에 ISACTIVE넣고 아닌 사용자만 가능
        //BRANCH 나머지 -> 해당 지부 사람들 모두

        //CAMPUS && NOTICE -> ROLE이 CAMPUS_STAFF, STAFF인 사람
        //CAMPUS && OB -> ENUM에 ISACTIVE넣고 아닌 사용자만 가능
        //CAMPUS && WORKBOOK -> ROLE이 CAMPUS_STAFF, STAFF인 사람만
        //CAMPUS 나머지 -> 해당 지부 사람들 모두



        boardRepository.save(Board.newBoard(member,
                request.getTitle(),
                request.getContent(),
                request.getHostType(),
                request.getBoardType()));

        return null;
    }



}
