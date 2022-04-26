package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception{
        //given
        Member member1=new Member();
        member1.setUsername("memberA");

        //when
        Long save = memberRepository.save(member1);
        Member findMember = memberRepository.find(save);
        //then
        Assertions.assertThat(findMember.getId())
                .isEqualTo(member1.getId());
        Assertions.assertThat(findMember.getUsername())
                .isEqualTo(member1.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member1); //
    }
}