package jpabook.jpashop.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    public void setMember(Member member){
        this.member=member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem:orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.IN_DELIVERY);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public void cancel(){
        if(delivery.getStatus()==DeliveryStatus.COMPLETED)throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        this.setOrderStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem:orderItems) {
            orderItem.cancel();
        }
    }
    //조회 로직//
    public int getTotalPrice(){
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice)
                .sum();
    }


}
