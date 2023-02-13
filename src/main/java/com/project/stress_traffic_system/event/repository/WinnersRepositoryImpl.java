package com.project.stress_traffic_system.event.repository;

import com.project.stress_traffic_system.event.model.QWinners;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;

import static com.project.stress_traffic_system.event.model.QWinners.winners;

public class WinnersRepositoryImpl implements WinnersRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private WinnersRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    //해당 이벤트의 당첨자수 가져오기
    @Override
    public Long countWinners(Long eventId) {
        return queryFactory
                .select(winners.count())
                .from(winners)
                .where(winners.event.id.eq(eventId))
                .fetchOne();

    }
}
