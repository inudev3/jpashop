package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(value=SpringExtension.class)
@Transactional//스프링의 어노테이션은 디폴트로 롤백한다.
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;
    @Test
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("kim");

        //when
        Long savedId = memberService.join(member);
        //then
        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));
    }
    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setUsername("kim1");

        Member member2 = new Member();
        member2.setUsername("kim1");
        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, ()-> memberService.join(member2));

    }
}