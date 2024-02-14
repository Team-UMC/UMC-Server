package com.umc.networkingService.domain.message.controller;


import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.message.dto.request.MessageRequest;
import com.umc.networkingService.domain.message.dto.response.MessageResponse;
import com.umc.networkingService.domain.message.facade.MessageFacade;
import com.umc.networkingService.domain.message.service.MessageRoomService;
import com.umc.networkingService.domain.message.service.MessageRoomServiceImpl;
import com.umc.networkingService.domain.message.service.MessageService;
import com.umc.networkingService.domain.message.service.MessageServiceImpl;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Tag(name = "채팅 API", description = "채팅 관련 API")
@RestController
@Validated
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {


    private final MessageFacade messageFacade;

    @Operation(summary = "쪽지 작성 API",description = "쪽지 작성 API")
    @PostMapping("/{messageRoomId}")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<MessageResponse.MessageId>
    postMessage(
            @CurrentMember Member member,
            @PathVariable UUID messageRoomId,
            @RequestBody MessageRequest.Message message
    ){
        return BaseResponse.onSuccess(messageFacade.postMessage(member, messageRoomId, message));
    }

    @Operation(summary = "쪽지 수정 API",description = "쪽지 수정 API")
    @PatchMapping("/{messageId}")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<MessageResponse.MessageId>
    patchMessage(
            @CurrentMember Member member,
            @PathVariable UUID messageId,
            @RequestBody MessageRequest.Message message
    ){
        return BaseResponse.onSuccess(messageFacade.patchMessage(member, messageId, message));
    }

    @Operation(summary = "쪽지 삭제 API",description = "쪽지 삭제 API")
    @DeleteMapping("/{messageId}")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<MessageResponse.MessageId>
    deleteMessage(
            @CurrentMember Member member,
            @PathVariable UUID messageId
    ){
        return BaseResponse.onSuccess(messageFacade.deleteMessage(member, messageId));
    }

    @Operation(summary = "쪽지 상세 조회 API",description = "쪽지 상세 조회 API (페이지 0부터 시작) (isAnonymous가 true일 경우 익명인 메시지))")
    @GetMapping("/{messageRoomId}")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<MessageResponse.JoinMessages>
    joinMessages(
            @CurrentMember Member member,
            @PathVariable UUID messageRoomId,
            @RequestParam(name= "page") int page //페이징 처리
    ){
        return BaseResponse.onSuccess(messageFacade.joinMessages(member, messageRoomId, page));
    }

    @Operation(summary = "쪽지함 조회 API",description = "쪽지함 조회 API (isAnonymous가 true일 경우 상대가 익명인 메시지)")
    @GetMapping("")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<MessageResponse.JoinMessageRooms>
    joinMessageRooms(
            @CurrentMember Member member
    ){
        return BaseResponse.onSuccess(messageFacade.joinMessageRooms(member));
    }

    @Operation(summary = "쪽지 시작하기 API",description = "쪽지 시작하기 API")
    @PostMapping("")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<MessageResponse.MessageRoomId>
    startMessage(
            @CurrentMember Member member,
            @RequestBody MessageRequest.StartMessageRoom messageRoom
    ){
        return BaseResponse.onSuccess(messageFacade.createMessageRoom(member, messageRoom.getMessageRoomUserId(), messageRoom.getIsAnonymous(), messageRoom.getMessageContent()));
    }

}
