package com.umc.networkingService.domain.proposal.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.proposal.entity.Proposal;
import com.umc.networkingService.domain.proposal.entity.QProposal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProposalRepositoryCustomImpl implements ProposalRepositoryCustom {

    private final JPAQueryFactory query;
    QProposal proposal = QProposal.proposal;

    @Override
    public Page<Proposal> findAllProposals(Member member, Pageable pageable){
        BooleanBuilder predicate = new BooleanBuilder();

        List<Proposal> proposals = query.selectFrom(proposal).where(predicate)
                .orderBy(proposal.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(proposals, pageable, query.selectFrom(proposal).where(predicate).fetch().size());
    }


}
