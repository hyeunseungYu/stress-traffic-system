package com.project.stress_traffic_system.facade;

import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.order.model.dto.OrderDto;
import com.project.stress_traffic_system.order.model.dto.OrderRequestDto;
import com.project.stress_traffic_system.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockStockFacade {
    private final RedissonClient redissonClient;
    private final OrderService orderService;

    public void decrease(Long key, int quantity) {
        RLock lock = redissonClient.getLock(key.toString());

        try {
            //얼마만큼이나 lock을 점유하고 있을 것인지
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
           //지정된 시간(여기에선 10초)동안 lock 획득을 못하면 false가 됨. 여기에서 문제가 발생할 수 있으니 wait타임을 적절하게 늘려주는 게 필요
            if (!available) {
                log.info("lock획득 실패");
                return;
            }

            orderService.decrease(key, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public OrderDto orderOne(Members member, OrderRequestDto requestDto) {
        RLock lock = redissonClient.getLock(requestDto.getProductId().toString());

        try {
            //얼마만큼이나 lock을 점유하고 있을 것인지
            //현재 점유시간 1초
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);
            if (!available) {
                log.info("lock획득 실패");
                return null;
            }

            return orderService.orderOne(member, requestDto);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            if (lock != null && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}

