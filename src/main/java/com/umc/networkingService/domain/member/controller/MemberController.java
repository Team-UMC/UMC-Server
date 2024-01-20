package com.umc.networkingService.domain.member.controller;

import com.umc.networkingService.config.security.auth.CurrentMember;
import com.umc.networkingService.domain.member.dto.request.MemberUpdateMyProfileRequest;
import com.umc.networkingService.domain.member.dto.response.*;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "멤버 API", description = "멤버 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "나의 프로필 수정 API", description = "본인 프로필 사진, 닉네임, 이름, 상태 메시지를 수정하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "IMAGE001", description = "이미지 S3 업로드 실패할 경우 발생")
    })
    @PostMapping("/update")
    public BaseResponse<MemberIdResponse> updateMyProfile(@CurrentMember Member member,
                                                          @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                          @RequestPart MemberUpdateMyProfileRequest request) {
        return BaseResponse.onSuccess(memberService.updateMyProfile(member, profileImage, request));
    }

    @Operation(summary = "유저 프로필 조회 API", description = "본인 또는 타인 프로필 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "MEMBER004", description = "조회 대상이 소속 대학교가 없는 경우 발생")
    })
    @Parameter(name = "memberId", in = ParameterIn.PATH, description = "조회하고자 하는 멤버의 UUID. 이 파라미터가 없으면 본인의 프로필을 조회하고, 있으면 해당 멤버의 프로필을 조회합니다.")
    @GetMapping(value = {"", "/{memberId}"})
    public BaseResponse<MemberInquiryProfileResponse> inquiryProfile(@CurrentMember Member member,
                                                                     @PathVariable(required = false) UUID memberId) {
        return BaseResponse.onSuccess(memberService.inquiryProfile(member, memberId));
    }

    @Operation(summary = "포인트 관련 유저 정보 조회 API", description = "닉네임, 프로필 사진, 기여도, 랭킹을 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "MEMBER004", description = "조회 대상이 소속 대학교가 없는 경우 발생")
    })
    @GetMapping("/simple")
    public BaseResponse<MemberInquiryHomeInfoResponse> inquiryHomeInfo(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.inquiryHomeInfo(member));
    }

    @Operation(summary = "깃허브 연동 API", description = "깃허브 로그인을 통해서 발급받은 accessToken으로 깃허브 닉네임을 저장하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "AUTH008", description = "깃허브 서버와 통신 실패할 경우 발생")
    })
    @PostMapping("/github")
    public BaseResponse<MemberAuthenticationGithubResponse> authenticationGithub(@CurrentMember Member member,
                                                                                 @RequestParam String code) {
        return BaseResponse.onSuccess(memberService.authenticationGithub(member, code));
    }

    @Operation(summary = "깃허브 데이터 조회 API", description = "깃허브 잔디 이미지를 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "AUTH008", description = "깃허브 인증이 안 된 사용자일 경우 발생")
    })
    @GetMapping("/github")
    public BaseResponse<MemberInquiryGithubResponse> inquiryGithubImage(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.inquiryGithubImage(member));
    }

    @Operation(summary = "남은 포인트 및 사용 내역 조회 API", description = "본인의 남은 포인트 및 최근 2개의 사용 내역를 조회하는 API입니다.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공")
    })
    @GetMapping("/points")
    public BaseResponse<MemberInquiryPointsResponse> inquiryMemberPoints(@CurrentMember Member member) {
        return BaseResponse.onSuccess(memberService.inquiryMemberPoints(member));

    }
}
