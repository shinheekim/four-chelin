package com.example.fourchelin.domain.member.repository.support;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MemberRepositoryCustomImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    public MemberRepositoryCustomImpl() {
        super(Member.class);
    }

    QMember member = QMember.member;

    public Optional<Member> findByPhone(String phone) {

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(member)
                .where(member.phone.eq(phone))
                .fetchOne());
    }


}
