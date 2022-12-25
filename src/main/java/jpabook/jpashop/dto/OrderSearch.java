package jpabook.jpashop.dto;

import jpabook.jpashop.domain.OrderStatus;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearch {//검색 시 입력할 회원 이름과 필터링할 주문 상태
    private String name;
    private OrderStatus status;
}
