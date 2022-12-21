package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(value= SpringExtension.class)
@Transactional
class OrderServiceTest {
    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;
    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember("kim");
        Book book = createBook("시골 JPA", 10000, 10);
        //when
        OrderItemDTO orderItemDTO = new OrderItemDTO(2, book.getId(), book.getPrice());
        Long orderId = orderService.order(member.getId(), orderItemDTO);
        //then
        Order order= orderRepository.findOne(orderId);
        assertEquals(order.getOrderStatus(), OrderStatus.IN_DELIVERY);
        assertEquals(order.getTotalPrice(), orderItemDTO.price()*orderItemDTO.count());
        assertEquals(order.getOrderItems().size(),1);
        assertEquals(book.getStockQuantity(),8);

    }

    private Book createBook(String name, int price, int quantity) {
        Book book = new Book();
        book.setAuthor("hi");
        book.setIsbn("1234");
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String username) {
        Member member = new Member();
        member.setUsername(username);
        member.setAddress(new Address("용구대로", "용인", "16865"));
        em.persist(member);
        return member;
    }

    @Test
    public void 주문취소() throws Exception{
        Member member = createMember("choi");
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount= 2;
        Long orderId =orderService.order(member.getId(), new OrderItemDTO(orderCount,item.getId(), item.getPrice()));
        assertEquals(item.getStockQuantity(), 8);

        orderService.cancelOrder(orderId);
        Order order = orderRepository.findOne(orderId);
        assertEquals(order.getOrderStatus(), OrderStatus.CANCEL);
        assertEquals(item.getStockQuantity(), 10);
    }
    @Test
    public void 상품주문_재고수량초과() throws Exception{
        Member member = createMember("jung");
        Item item = createBook("hi", 10000,10);
        int orderCount = 11;
        //when

        //then
        assertThrows(NotEnoughStockException.class, ()->orderService.order(member.getId(), new OrderItemDTO(orderCount,item.getId(), item.getPrice())));
    }
}