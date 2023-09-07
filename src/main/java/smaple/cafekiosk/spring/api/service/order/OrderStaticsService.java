package smaple.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smaple.cafekiosk.spring.api.service.MailService;
import smaple.cafekiosk.spring.domain.order.Order;
import smaple.cafekiosk.spring.domain.order.OrderRepository;
import smaple.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStaticsService  {

    private final OrderRepository orderRepository;
    private final MailService mailService;
    public void sendOrderStaticsMail(LocalDate orderDate, String email)  {
        //해당일자에 결제완료된 주문들을 가져와서
        List<Order> orders = orderRepository.findOrdersBy(
                //주문 등록시간을 기준으로 하루치 주문을 검색하기
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.COMPLETED
        );

        //총 매출 합계를 계산하고
        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        //메일 전송
        boolean result = mailService.sendMail(
                "no-reply@cafekiosk.com",
                email,
                String.format("[매출 통계] %s", orderDate),
                String.format("총 매출 합계는 %s 원입니다.", totalAmount)
        );
        if (!result){
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        }



    }
}
