package jpabook.jpashop.dto;

import jpabook.jpashop.domain.OrderStatus;

public record OrderSearch(String memberName, OrderStatus status) {//검색 시 입력할 회원 이름과 필터링할 주문 상태

}
