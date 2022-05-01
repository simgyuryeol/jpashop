package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//readonly true를 할 경우 읽기에서 성능이 최적화 되지만 읽기만 가능함으로 쓰기는 불가능하다. 필요한 경우 따로 트랜잭션을 입력한다
//데이터 변경은 트랜잭션이 필요하다
//javax와 spring 2개가 있는데 스프링이 사용할 기능이 더많다
//@AllArgsConstructor //lombok으로 모든 필드를 가지고 MemberService 생성자를 만들어준다
@RequiredArgsConstructor //final이 있는 필드를 가지고 생성자를 만들어준다. 최종적으로 이것과 final 필드를 사용하는 것이 좋다
public class MemberService {
    //@Autowired
    private final MemberRepository memberRepository; //수정할 일이 없기 때문에 final을 사용해준다.

//    @Autowired //테스트를 할 경우 값을 바꿔가면서 사용 가능하다. 하지만, 실행중에 누군가가 바꿀 수 있다.
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //@Autowired //그래서 set보다는 생성자를 사용하여 값을 바꿀 수 없다
    //생성자가 1개일 경우에는 Autowired를 생략해도 사용 가능하다
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }


    //회원 가입
    @Transactional //디폴트가 readonly false이므로 따로 써주면 쓰기도 가능하다
    public Long join(Member member){

        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //중복 회원이면 exception 이경우에는 동시에 접근할 경우 예외처리가 안됨으로 실무에서는 최후로 데베에 유니크설정도 해준다.
        List<Member> findmembers = memberRepository.findbyName(member.getName());
        if (!findmembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
