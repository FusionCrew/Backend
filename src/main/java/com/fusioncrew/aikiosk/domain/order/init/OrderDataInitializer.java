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
        public void run(String... args) throws Exception {
                if (orderRepository.count() == 0) {
                        // 주문 1: 불고기버거 2개
                        Order order1 = new Order("ord_001", 7000, "sess_001");
                        order1.updateStatus(OrderStatus.CONFIRMED);
                        order1.addOrderItem(OrderItem.builder()
                                        .menuItemId("menu_001")
                                        .name("불고기버거")
                                        .price(3500)
                                        .quantity(2)
                                        .order(order1)
                                        .build());
                        orderRepository.save(order1);

                        // 주문 2: 새우버거 1개 + 콜라 1개
                        Order order2 = new Order("ord_002", 6000, "sess_002");
                        order2.updateStatus(OrderStatus.CONFIRMED);
                        order2.addOrderItem(OrderItem.builder()
                                        .menuItemId("menu_002")
                                        .name("새우버거")
                                        .price(4500)
                                        .quantity(1)
                                        .order(order2)
                                        .build());
                        order2.addOrderItem(OrderItem.builder()
                                        .menuItemId("menu_003")
                                        .name("콜라")
                                        .price(1500)
                                        .quantity(1)
                                        .order(order2)
                                        .build());
                        orderRepository.save(order2);

                        // 주문 3: 치즈버거 세트 (조리 중)
                        Order order3 = new Order("ord_003", 8500, "sess_003");
                        order3.updateStatus(OrderStatus.MAKING);
                        order3.addOrderItem(OrderItem.builder()
                                        .menuItemId("menu_004")
                                        .name("치즈버거 세트")
                                        .price(8500)
                                        .quantity(1)
                                        .order(order3)
                                        .build());
                        orderRepository.save(order3);

                        // 주문 4: 빅맥 (준비 완료)
                        Order order4 = new Order("ord_004", 6500, "sess_004");
                        order4.updateStatus(OrderStatus.READY);
                        order4.addOrderItem(OrderItem.builder()
                                        .menuItemId("menu_005")
                                        .name("빅맥")
                                        .price(6500)
                                        .quantity(1)
                                        .order(order4)
                                        .build());
                        orderRepository.save(order4);

                        System.out.println("✅ 초기 주문 상세 데이터가 생성되었습니다.");
                }
        }
}
