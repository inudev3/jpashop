package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {


    private final OrderService orderService;
    private final OrderRepository orderRepository;
    @GetMapping("/api/v2/orders")
    public List<OrderDto> orders(){
        return orderService.findOrders(new OrderSearch()).stream().map(OrderDto::new).collect(Collectors.toList());
    }
    @GetMapping("/api/v3/orders")//fetch join
    public List<OrderDto> ordersV3(){
        return orderRepository.findAllWithItem().stream().map(OrderDto::new).collect(Collectors.toList());
    }
    @GetMapping("/api/v3.1/orders")//paging
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset",defaultValue = "0") int offset,
                                        @RequestParam(value = "limit",defaultValue = "100") int limit
                                        ){

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);//ToOne 관계만 fetch join
        return orders.stream().map(OrderDto::new).collect(Collectors.toList());
    }

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
