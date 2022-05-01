package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //junit 실행할 때 스프링이랑 같이 실행하겠다는 뜻
@SpringBootTest //스프링 부트를 띄운 상태로 실행하려면 필요
@Transactional //테스트가 끝나면 롤백 기능 실행
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    //@Autowired EntityManager em;

    @Test //트랜잭션 기능으로 인해 테스트가 끝나면 롤백을 해서 insert문이 없다
    //@Rollback(value = false) //롤백을 취소할 수 있다
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("sim");

        //when
        Long savedId = memberService.join(member);

        //then
        //em.flush(); //flush를 하면 쿼리를 강제로 날리는 것이므로 insert문을 볼 수 잇음
        Assertions.assertEquals(member,memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class) //try,catch를 하지 않아도 예외처리가 가능하다.
    public void 중복회원예약() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        //when
        memberService.join(member1);
        //try {
        memberService.join(member2);
        //}catch (IllegalStateException e){
        //    return;
        //}

        //then
        fail("예외가 발생해야 한다");
    }

}