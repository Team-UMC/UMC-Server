package com.umc.networkingService.domain.test.controller;

import com.umc.networkingService.domain.test.dto.TestRequest;
import com.umc.networkingService.domain.test.dto.TestResponse;
import com.umc.networkingService.domain.test.service.TestService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "예제 API", description = "테스트용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final TestService testService;


    @Operation(summary = "성공적인 응답 반환 API", description = "테스트 문자열을 반환하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "테스트 문자열을 성공적으로 반환")
    @GetMapping("/success")
    public BaseResponse<TestResponse.TempTestDTO> successResponseAPI() {
        return BaseResponse.onSuccess(TestResponse.TempTestDTO.builder().testString("This is Test!").build());
    }



    @Operation(summary = "예외처리 API", description = "flag 값에 따라 예외를 발생시키는 API입니다.")
    @Parameter(name = "flag", description = "1인 경우 예외처리", example = "1", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "TEMP4001", description = "예외 처리 테스트입니다.")
    })
    @GetMapping("/exception")
    public BaseResponse<TestResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
        testService.CheckFlag(flag);
        return BaseResponse.onSuccess(TestResponse.TempExceptionDTO.builder()
                .flag(flag)
                .build());
    }

    @Operation(summary = "유효성 검사 API", description = "요청 객체의 유효성을 검사하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "COMMON400", description = "testString가 공백일 경우")
    })
    @PostMapping("/validation")
    public BaseResponse<TestResponse.TempTestDTO> testValidationAPI(
            @RequestBody @Valid TestRequest.TempTestRequest request) {
        return BaseResponse.onSuccess(TestResponse.TempTestDTO.builder()
                .testString(request.getTestString())
                .build());
    }
}