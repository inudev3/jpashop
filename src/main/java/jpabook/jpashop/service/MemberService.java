package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {
    @Autowired //인젝션 방법 중 하나

    private MemberRepository memberRepository;

    public Long join(Member member){
        validDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validDuplicateMember(Member member) {
        List<Member> findMember = memberRepository.findAllByName(member.getUsername());
        if(!findMember.isEmpty())throw new IllegalStateException("이미 존재하는 회원입니다");
    }
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

}
