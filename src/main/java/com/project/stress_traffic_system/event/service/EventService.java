package com.project.stress_traffic_system.event.service;

import com.project.stress_traffic_system.event.model.Event;
import com.project.stress_traffic_system.event.repository.EventRepository;
import com.project.stress_traffic_system.members.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    //선착순 이벤트
    public String createEvent(Members member) {

        if (eventRepository.count() >= 1000) {
            return "선착순 이벤트가 마감되었습니다";
        }

        Event event = new Event(member);
        Event saveEvent = eventRepository.save(event);
        return "이벤트 신청 성공! 당첨번호: " + saveEvent.getId();
    }
}
