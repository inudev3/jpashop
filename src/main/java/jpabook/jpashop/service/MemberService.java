package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class MemberService {

    
    private final MemberRepository memberRepository;

    @Transactional
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

    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }

    @Transactional
    public Long update(Long id, String name) {//Member
        Member member = memberRepository.findOne(id);
        member.setUsername(name);
        return member.getId();
    }
}
