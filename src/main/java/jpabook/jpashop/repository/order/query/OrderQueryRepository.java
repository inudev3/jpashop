package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;
    public List<OrderQueryDto> findOrderQuerDto(){
        List<OrderQueryDto> orders = findOrders();
        orders.forEach(o->o.setOrderItems(findOrderItems(o.getOrderId())));
        return orders;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("""
        select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice,oi.count) 
        from OrderItem oi join oi.item i 
        where oi.order.id =:orderId
""", OrderItemQueryDto.class).setParameter("orderId", orderId).getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery("""
                    select  new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.username, o.orderDate,o.orderStatus, d.address)from Order o join o.member m join o.delivery d 
                """, OrderQueryDto.class).getResultList();
    }
}
