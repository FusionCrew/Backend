package com.fusioncrew.aikiosk.domain.order.init;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderItem;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDataInitializer implements CommandLineRunner {

    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) {
        if (orderRepository.count() == 0) {

            // 주문 1
            Order order1 = new Order();
            order1.setSessionId("sess_001");
            order1.setStatus(OrderStatus.CONFIRMED);

            OrderItem oi11 = new OrderItem();
            oi11.setMenuItemId(1L);
            oi11.setName("불고기버거");
            oi11.setPrice(3500);
            oi11.setQuantity(2);
            oi11.setOptionsJson("{\"name\":\"불고기버거\"}");

            order1.addItem(oi11);
            orderRepository.save(order1);

            // 주문 2
            Order order2 = new Order();
            order2.setSessionId("sess_002");
            order2.setStatus(OrderStatus.CONFIRMED);

            OrderItem oi21 = new OrderItem();
            oi21.setMenuItemId(2L);
            oi21.setName("새우버거");
            oi21.setPrice(4000);
            oi21.setQuantity(1);
            oi21.setOptionsJson("{\"name\":\"새우버거\"}");

            OrderItem oi22 = new OrderItem();
            oi22.setMenuItemId(3L);
            oi22.setName("콜라");
            oi22.setPrice(1500);
            oi22.setQuantity(1);
            oi22.setOptionsJson("{\"name\":\"콜라\"}");

            order2.addItem(oi21);
            order2.addItem(oi22);
            orderRepository.save(order2);

            // 주문 3 (조리 중)
            Order order3 = new Order();
            order3.setSessionId("sess_003");
            order3.setStatus(OrderStatus.MAKING);

            OrderItem oi31 = new OrderItem();
            oi31.setMenuItemId(4L);
            oi31.setName("치즈버거 세트");
            oi31.setPrice(6500);
            oi31.setQuantity(1);
            oi31.setOptionsJson("{\"name\":\"치즈버거 세트\"}");

            order3.addItem(oi31);
            orderRepository.save(order3);

            // 주문 4 (준비 완료)
            Order order4 = new Order();
            order4.setSessionId("sess_004");
            order4.setStatus(OrderStatus.READY);

            OrderItem oi41 = new OrderItem();
            oi41.setMenuItemId(5L);
            oi41.setName("빅맥");
            oi41.setPrice(5500);
            oi41.setQuantity(1);
            oi41.setOptionsJson("{\"name\":\"빅맥\"}");

            order4.addItem(oi41);
            orderRepository.save(order4);

            System.out.println("✅ 초기 주문 데이터가 생성되었습니다.");
        }
    }
}