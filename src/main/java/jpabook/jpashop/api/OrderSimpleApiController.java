package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.dto.SimpleOrderQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orders(){
        List<Order> orders = orderService.findOrders(new OrderSearch());
        return orders; //엔티티 그대로 반환
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderQueryDto>ordersV2(){
        List<Order> orders = orderService.findOrders(new OrderSearch());
        return orders.stream().map(SimpleOrderQueryDto::new).collect(Collectors.toList()) ;
    }


    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderQueryDto> orderList(){
        return orderRepository.findAllWithMemberDelivery().stream()
                .map(SimpleOrderQueryDto::new)
                .collect(Collectors.toList());
    }
    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> orderDtoList(){
        return orderRepository.findOrderDtos();
    }
}
