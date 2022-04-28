package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY) //1대1 관계에서는 어디에 두든 상관없다 개발자가 편한 곳에 둔다. 여기선 order에 연관관계의 주인으로 둔다
    private Order order;

    @Embedded //내장 타입
    private Address address;

    @Enumerated(EnumType.STRING)
    //enum 타입을 사용할 때 enumerated를 사용하는데 이는 ordinal이랑 string을 넣을 수 있다. 디폴트는 ordinal로 1,2,3 숫자로 들어감
    // 하지만 문제는 증간에 다른 (READY, XXX, COMP) 같이 상태값이 들어가면 생기면 오류가 난다, 그래서 String으로 사용해야됨, 그래야 중간에 밀리는게 없다
    private DeliveryStatus status; //READY, COMP

}
