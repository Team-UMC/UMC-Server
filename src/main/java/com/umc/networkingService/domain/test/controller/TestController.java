package com.umc.networkingService.domain.test.controller;

import com.umc.networkingService.domain.test.dto.TestRequest;
import com.umc.networkingService.domain.test.dto.TestResponse;
import com.umc.networkingService.domain.test.service.TestService;
import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    @Operation(summary = "문자열 반복", description = "파라미터로 받은 문자열을 2번 반복합니다.")
    @Parameter(name = "str", description = "2번 반복할 문자열")
    @GetMapping("/returnStr")
    public String returnStr(@RequestParam String str) {
        return str + "\n" + str;
    }

    @GetMapping("/example")
    public String example() {
        return "예시 API";
    }

    @Hidden
    @GetMapping("/ignore")
    public String ignore() {
        return "무시되는 API";
    }

    private final TestService tempService;

    @GetMapping("/test")
    public BaseResponse<TestResponse.TempTestDTO> testAPI() {

        return BaseResponse.onSuccess(TestResponse.TempTestDTO.builder().testString("This is Test!").build());
    /*
    {
      "timestamp": "2024-01-12T17:19:02.0172718",
      "code": "COMMON200",
      "message": "요청에 성공하였습니다.",
      "result": {
        "testString": "This is Test!"
      }
    }
     */
    }

    @GetMapping("/exception")
    public BaseResponse<TestResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
        tempService.CheckFlag(flag);
        return BaseResponse.onSuccess(TestResponse.TempExceptionDTO.builder()
                .flag(flag)
                .build());
    }
    /*
    {
      "timestamp": "2024-01-12T17:27:10.1903229",
      "code": "TEMP4001",
      "message": "예외처리 테스트입니다."
    }
     */

    @PostMapping("/test-validation")
    public BaseResponse<TestResponse.TempTestDTO> testValidationAPI(
            @RequestBody @Valid TestRequest.TempTestRequest request) {
        return BaseResponse.onSuccess(TestResponse.TempTestDTO.builder()
                .testString(request.getTestString())
                .build());
    }
    /* testString ""로 주었을 경우
    {
      "timestamp": "2024-01-12T17:47:55.8893269",
      "code": "COMMON400",
      "message": "잘못된 요청입니다.",
      "result": {
        "testString": "testString은 필수입니다."
      }
     }
     */
}