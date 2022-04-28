package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders") //DB상 order은 예약어 임으로 orders 사용
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne //하나의 회원이 여러개의 상품을 주문함으로 다대1
    @JoinColumn(name="member_id") //외래키값
    private Member member;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //cascade는 orderItems를 저장하면 order도 같이 저장된다.
    // 만약에 쓰지않으면 orderitem을 저장하고 따로 order도 저장해야 한다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //1대1
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //enum 주문의 상태 [ORDER, CANCEL]

    //== 연관관계 편의 메서드

    public void setMember(Member member){
        this.member=member;
        member.getOrders().add(this); //하나로 양방향 연관관계를 저장할 수 있다. 핵심적으로 컨트롤 하는쪽에 넣는게 좋다
    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery=delivery;
        delivery.setOrder(this);
    }

}
