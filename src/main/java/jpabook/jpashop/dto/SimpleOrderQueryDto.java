package jpabook.jpashop.dto;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SimpleOrderQueryDto {

    public SimpleOrderQueryDto(Order order) {
        this.orderId = order.getId();
        this.name=order.getMember().getUsername();
        this.orderDate=order.getOrderDate();
        this.orderStatus=order.getOrderStatus();
        this.address=order.getDelivery().getAddress();;
    }

    private final Long orderId;
    private final String name;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderDate;
    private final Address address;
}
