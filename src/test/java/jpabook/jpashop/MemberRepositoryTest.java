package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional //javax와 스프링 중 스프링을 쓰면 됨
    public void testMember() throws Exception{
        //given
        Member member=new Member();
        member.setUsername("hello");

        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findOne(member.getId()); //select 쿼리 안나감

        //then
        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(),member.getUsername());
        assertEquals(findMember, member); //동일 영속성 컨텍스트, 동일 식별자
    }

}