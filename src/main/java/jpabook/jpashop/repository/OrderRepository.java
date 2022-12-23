package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;
    public void save(Order order){
        em.persist(order);
    }
    public Order findOne(Long id){
        return em.find(Order.class, id);
    }
    /*
    * jpa criteria 예시(실무 사용하지 않음)
    * */
    public List<Order> findAll(OrderSearch orderSearch){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);
        List<jakarta.persistence.criteria.Predicate> criteria = new ArrayList<>();
        if(orderSearch.memberName()!=null){
            Predicate name = cb.like(m.get("username"), "%"+orderSearch.memberName()+"%");
            criteria.add(name);
        }
        if(orderSearch.status()!=null){
            Predicate status = cb.equal(o.get("orderStatus"), orderSearch.status());
            criteria.add(status);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }//dto 사용한 검
}
