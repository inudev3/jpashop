package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //jpa 표준 어노테이션인 @PersistenceContext를 사용해야만 인젝션되지만, 스프링 data jpa는 autowired 해줌
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findAllByName(String name){
        return em.createQuery("select m from Member m where m.username=:name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
