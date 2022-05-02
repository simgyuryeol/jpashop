package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders") //DB상 order은 예약어 임으로 orders 사용
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //proctected 생성자를 생성하여 새로운 생성을 막는다
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


    // 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){ //orderitem을 ...으로 여러개 넘김
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); //orderstatus를 처음 상태로 둠
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직
    /**
     * 주문 취소
     */
    public void cancel(){
        if (delivery.getStatus()==DeliveryStatus.COMP) { //배송 완료 상태
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    // 조회 로직

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();

//        int totalPrice=0;
//        for (OrderItem orderItem : orderItems){
//            totalPrice+=orderItem.getTotalPrice();
//        }
//        return totalPrice;
    }

}
