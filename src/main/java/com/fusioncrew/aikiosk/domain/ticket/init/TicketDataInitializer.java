package com.fusioncrew.aikiosk.domain.ticket.init;

import com.fusioncrew.aikiosk.domain.ticket.entity.Ticket;
import com.fusioncrew.aikiosk.domain.ticket.entity.TicketStatus;
import com.fusioncrew.aikiosk.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketDataInitializer implements CommandLineRunner {

    private final TicketRepository ticketRepository;

    @Override
    public void run(String... args) {
        try {
            if (ticketRepository.count() == 0) {
                for (int i = 1; i <= 10; i++) {
                    Ticket ticket = Ticket.builder()
                            .orderId("ord_0001")
                            .paymentId("pay_0001")
                            .number(i)
                            .status(TicketStatus.WAITING)
                            .priority("NORMAL")
                            .build();
                    ticketRepository.save(ticket);
                }
                System.out.println("✅ 초기 티켓 데이터 10개가 생성되었습니다.");
            }
        } catch (Exception e) {
            System.err.println("❌ 티켓 데이터 초기화 실패 (무시하고 계속 진행): " + e.getMessage());
        }
    }
}
