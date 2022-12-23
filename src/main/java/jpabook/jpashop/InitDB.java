package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;
    @PostConstruct
    public void init(){
        initService.init1();
        initService.init2();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        public void init1(){
            Member member = createMember("userA", "용구대로", "yongin");
            em.persist(member);

            Book book1 = createBook("JPA1", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2", 20000, 200);
            em.persist(book2);

            OrderItem orderItem1= OrderItem.createOrderItem(book1, 1, 10000);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 2, 20000);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }



        public void init2(){
            Member member = createMember("userB", "진주", "seoul");
            em.persist(member);

            Book book1 = createBook("SPRING 1",10000,100);
            em.persist(book1);

            Book book2 = createBook("SPRING 2", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1= OrderItem.createOrderItem(book1, 1, 10000);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 2, 20000);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }



        private static Member createMember(String name, String street, String city) {
            Member member = new Member();
            member.setUsername(name);
            member.setAddress(new Address(street, city, "16865"));
            return member;
        }
        private static Book createBook(String name, int price, int stockQuantity) {
            Book book2 = new Book();
            book2.setName(name);
            book2.setPrice(price);
            book2.setStockQuantity(stockQuantity);
            return book2;
        }
    }
}
