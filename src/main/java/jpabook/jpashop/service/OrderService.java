package jpabook.jpashop.service;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    /*
    주문
     */
    @Transactional//데이터 변환은 transaction 필요
    public Long order(Long memberId, OrderItemDTO... orderItems){
        Member member = memberRepository.findOne(memberId);
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDTO orderItemDTO: orderItems ) {
            Item item = itemRepository.findOne(orderItemDTO.itemId());
            orderItemList.add(OrderItem.createOrderItem(item, orderItemDTO.count(), item.getPrice()));
        }

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        Order order = Order.createOrder(member,delivery, orderItemList.toArray(new OrderItem[0]));
        orderRepository.save(order);//order가 delivery와 orderitems를 cascade 전이로 가지기 때문에 order만 persist하면 (나머지 엔티티는
        //레포지토리에 저장할 필요 X
        //cascade는 참조하는 곳이 한 곳일 경우에 사용하면 좋다.

        return order.getId();
    }
    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId){
        Order cancelOrder = orderRepository.findOne(orderId);
        cancelOrder.cancel();
    }
    //주문 검색(동적 쿼리)
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }


}
