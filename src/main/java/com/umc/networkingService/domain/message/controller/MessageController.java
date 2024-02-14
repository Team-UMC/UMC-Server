package com.umc.networkingService.domain.message.controller;


import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.message.dto.request.MessageRequest;
import com.umc.networkingService.domain.message.dto.response.MessageResponse;
import com.umc.networkingService.domain.message.service.MessageRoomServiceImpl;
import com.umc.networkingService.domain.message.service.MessageServiceImpl;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Tag(name = "채팅 API", description = "채팅 관련 API")
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageServiceImpl messageService;
    private final MessageRoomServiceImpl messageRoomService;

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
        return BaseResponse.onSuccess(messageService.postMessage(member, messageRoomId, message));
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
        return BaseResponse.onSuccess(messageService.patchMessage(member, messageId, message));
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
        return BaseResponse.onSuccess(messageService.deleteMessage(member, messageId));
    }

    @Operation(summary = "쪽지 상세 조회 API",description = "쪽지 상세 조회 API")
    @GetMapping("/{messageRoomId}")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<MessageResponse.JoinMessages>
    joinMessages(
            @CurrentMember Member member,
            @PathVariable UUID messageRoomId,
            @RequestParam(name= "page") Long page //페이징 처리 (1부터 시작)
    ){
        return BaseResponse.onSuccess();
    }

    @Operation(summary = "쪽지함 조회 API",description = "쪽지함 조회 API")
    @GetMapping("")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<MessageResponse.JoinMessageRooms>
    joinMessageRooms(
            @CurrentMember Member member
    ){
        return BaseResponse.onSuccess(messageRoomService.joinMessageRooms(member));
    }

    @Operation(summary = "쪽지 시작하기 API",description = "쪽지 시작하기 API")
    @PostMapping("/{receiverId}")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    public BaseResponse<MessageResponse.MessageRoomId>
    startMessage(
            @CurrentMember Member member,
            @PathVariable UUID receiverId,
            @RequestParam Boolean isAnonymous
    ){
        return BaseResponse.onSuccess(messageRoomService.createMessageRoom(member, receiverId, isAnonymous));
    }

}
