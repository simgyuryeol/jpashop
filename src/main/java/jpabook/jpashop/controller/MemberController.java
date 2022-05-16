package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){ //Model은
        model.addAttribute("memberForm",new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        //원래는 화이트 페이지가 뜨는데 BindingResult는 result에 오류가 담겨서 이 코드가 실행됨

        if (result.hasErrors()){ //오류가 있으면 해당 코드 실행
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/"; //첫번째 페이지로 넘어감
    }

    @GetMapping("/members")
    public String list(Model model){ //좀 복잡해지면 Model 엔티티를 쓰는 것이 아닌 DTO를 사용해서 필요한 값만 받는 것이 좋다
        //api를 만들 경우에는 절대 엔티티를 외부로 반환하면 안되다. 데이터가 노출되고, api스펙이 변하게 되는 2가지 이유때문에 안된다.
        // 템플릿 엔진에서는 이렇게 엔티티로 사용해도 괜찮다. 그래도 가장 권장하는 것은 DTO를 사용하는 것이 좋다
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }


}
