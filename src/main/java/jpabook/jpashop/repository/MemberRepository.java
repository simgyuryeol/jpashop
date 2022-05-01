package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //@PersistenceContext //@RequiredArgsConstructor를 사용하여 생락할 수 있다.
    // 생성자 인젝션이지만 생략 가능하다
    //public MemberRepository(EntityManager em) {
//        this.em = em;
//    }
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);//jpa가 memeber를 저장
    }

    public Member findOne(Long id){
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
        //jql은 sql과 달리 테이블이 아닌 객체를 대상으로 조회함
    }

    public List<Member> findbyName(String name){
        return em.createQuery("select m from Member m where m.name= :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }

}
