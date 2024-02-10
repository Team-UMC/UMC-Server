package com.umc.networkingService.domain.proposal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProposalPagingResponse {
    private int page;
    private List<ProposalPageResponse> proposalPageResponses = new ArrayList<>();
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;
    private int totalElements;
}
