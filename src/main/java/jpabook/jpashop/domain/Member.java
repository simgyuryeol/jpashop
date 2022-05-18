package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue //시퀀스 값을 사용
    @Column(name = "member_id")
    private Long id;

    //@NotEmpty //api를 사용할 때 name값 필수로 사용. 하지만 어떤 api는 필수가 아닐 수 있으므로 따로 dto를 만들어 설정하는 것이 좋다.
    private String name;

    @Embedded //Embedded나 Embeddable 둘중 하나만 있어도 된다
    private Address address;

    @OneToMany(mappedBy = "member") //멤버 입장에서는 반대로 1대다, 연관관계의 주인이 아니라는 뜻
    private List<Order> orders = new ArrayList<>();
 }
