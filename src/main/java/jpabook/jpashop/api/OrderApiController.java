package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.OrderService;
import lombok.Data;
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
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getUsername();
            order.getDelivery().getAddress();
            order.getOrderItems().stream().forEach(o->o.getItem().getName());
        }
        return all;
    }
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        return orderRepository.findAll(new OrderSearch()).stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }
    @Getter
    static class OrderDto{
        private Long orderId;
        private OrderStatus orderStatus;
        private LocalDateTime orderDate;
        private String name;
        private Address address;
        private List<OrderItem> orderItems;
        public OrderDto(Order order) {
            orderId=order.getId();
            orderStatus=order.getOrderStatus();
            orderDate=order.getOrderDate();
            address=order.getDelivery().getAddress();
            name=order.getMember().getUsername();
            orderItems=order.getOrderItems();
        }
    }
}
