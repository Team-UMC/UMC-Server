package com.umc.networkingService.domain.exceptionTest;

import com.umc.networkingService.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
@Tag(name = "예제 API", description = "response exception 테스트용 API")
public class TempController {
    private final TempService tempService;

    @GetMapping("/test")
    public BaseResponse<TempResponse.TempTestDTO> testAPI() {

        return BaseResponse.onSuccess(TempResponse.TempTestDTO.builder().testString("This is Test!").build());
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
    public BaseResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
        tempService.CheckFlag(flag);
        return BaseResponse.onSuccess(TempResponse.TempExceptionDTO.builder()
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
    public BaseResponse<TempResponse.TempTestDTO> testValidationAPI(
            @RequestBody @Valid TempRequest.TempTestRequest request) {
        return BaseResponse.onSuccess(TempResponse.TempTestDTO.builder()
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