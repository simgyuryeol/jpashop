package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //proctected 생성자를 생성하여 새로운 생성을 막는다
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")//하나의 오더가 여러개의 오더아이템을 가진다
    private Order order;

    private int orderPrice; //주문 가격
    private int count; //주문수량


    //생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    // 비즈니스 로직
    public void cancel() {
        getItem().addStock(count); //재고 수량 원복
    }

    //조회 로직
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
