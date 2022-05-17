package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */

    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        // 생성 포맷을 마음대로 설정하게되면 유지보수가 힘들기 때문에 새로운 포맷으로 만들지 못하게 해야한다.
        // 해당 클래스에 protect 생성자를 설정하면 새롭게 만들지 못한다.
        // new Order() -> 오류 발생
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        // 원래는 deliverrepository를 따로 만들고 jpa에 넣어준 다음에 값을 세팅하는데 orderRepository 하나만 했다.
        // 왜냐하면 cascade ALL 옵션으로 했기 때문에 Order를 persiste하면 안에 있는 해당 delivery나 orderitem이 persist가 된다.
        // order가 deivery와 orderitem이 관리를 하기 때문에 이럴 때 사용하는게 좋다. order만 이들을 참조하기 때문에 사용하기 좋다
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
        //jpa를 활용하면 이렇게 엔티티안에 데이터만 바꾸면 dirty checking(변경 내역 감지)를 하여 변경 내역을 데이터베이스에 업데이트 쿼리가 날아간다.
        // 그래서 따로 업데이트를 하지 않아도 된다.
    }



  public List<Order> findOrders(OrderSearch orderSearch){
       return orderRepository.findAllByCriteria(orderSearch);
   }
}
