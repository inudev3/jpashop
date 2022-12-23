package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//기본 생성자 사용 방지
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private int orderPrice;
    private int count;

    //팩토리 메서드//
    public static OrderItem createOrderItem(Item item, int count, int orderPrice){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(orderPrice);
        item.removeStock(count);
        return orderItem;
    }
    //비즈니스 로직//
    public void cancel() {
        this.getItem().addStock(count);//재고 수량 원복
    }
    public int getTotalPrice(){
        return getOrderPrice()*getCount();
    }
}