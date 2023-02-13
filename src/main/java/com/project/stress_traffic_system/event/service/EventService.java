package com.project.stress_traffic_system.event.service;

import com.project.stress_traffic_system.event.model.Event;
import com.project.stress_traffic_system.event.model.Winners;
import com.project.stress_traffic_system.event.repository.EventRepository;
import com.project.stress_traffic_system.event.repository.WinnersRepository;
import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ProductRepository productRepository;
    private final WinnersRepository winnersRepository;

    //선착순 이벤트
    @Transactional
    public String applyEvent(Members member, Long eventId) {

        if (getWinnersCount(eventId) >= 1000) {
            return "선착순 이벤트가 마감되었습니다";
        }

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new IllegalArgumentException("이벤트가 존재하지 않습니다")
        );
        Winners winner = new Winners(event, member, (int) (getWinnersCount(eventId) + 1));
        Winners savedWinner = winnersRepository.save(winner);

        return "이벤트 신청 완료! 당첨번호 : " + savedWinner.getNum();
    }

    @Transactional
    public void createEvent(Members member, Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다")
        );

        Event event = new Event(product);
        eventRepository.save(event);
    }

    private Long getWinnersCount(Long eventId) {
        return winnersRepository.countWinners(eventId);
    }
}
