package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.api.OrderSimpleApiController;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.dto.SimpleOrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /*
     * jpa criteria 예시(실무 사용하지 않음)
     * */
    public List<Order> findAll(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);
        List<jakarta.persistence.criteria.Predicate> criteria = new ArrayList<>();
        if (orderSearch.getName() != null) {
            Predicate name = cb.like(m.get("username"), "%" + orderSearch.getName() + "%");
            criteria.add(name);
        }
        if (orderSearch.getStatus() != null) {
            Predicate status = cb.equal(o.get("orderStatus"), orderSearch.getStatus());
            criteria.add(status);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }//dto 사용한 검

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("""
                    select o from Order o join fetch o.member m join fetch o.delivery d
                """, Order.class).getResultList();
    }

    public List<SimpleOrderQueryDto> findOrderDtos() {
        List<SimpleOrderQueryDto> resultList = em.createQuery("""
                select new jpabook.jpashop.dto.SimpleOrderQueryDto(o.id, m.username,o.orderStatus,o.orderDate,d.address) from Order o 
                join o.member m 
                join o.delivery d
                    """, SimpleOrderQueryDto.class).getResultList();
        return resultList; //아예 dto
    }
}
