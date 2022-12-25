package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {


    private final OrderService orderService;

    @GetMapping("/api/v2/orders")
    public List<OrderDto> orders(){}

    @Getter
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getUsername();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getOrderStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
        }
    }
    @Getter
    static class  OrderItemDto{
        private String name;
        private int price;
        private int count;
        public OrderItemDto(OrderItem orderItem) {
            price=orderItem.getOrderPrice();
            count=orderItem.getCount();
            name=orderItem.getItem().getName();
        }
    }
}
