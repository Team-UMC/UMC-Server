package com.umc.networkingService.domain.proposal.dto.request;

import com.umc.networkingService.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProposalCreateRequest {
    @NotBlank(message = "title은 필수 입력값입니다.")
    private String title;

    @NotBlank(message = "content는 필수 입력값입니다.")
    private String content;
}
