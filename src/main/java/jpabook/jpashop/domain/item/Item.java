package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)//테이블 전략을 사용하여 하나의 테이블에 상속관계를 사용
@DiscriminatorColumn(name = "dtype") //싱글 테이블이므로 이를 비교하기 위한 값 설정
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany
    @JoinTable(name = "categroy_item",
        joinColumns = @JoinColumn(name = "categroy_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Category> categories = new ArrayList<>();

    //비즈니스 로직
    // get,set을 이용해 데이터를 빼내서 더해서 set으로 다시 입력하는 것보다
    // 데이터를 가지고 있는 쪽에 비즈니스 로직이 있는 것이 객체지향적으로 좋다.

    // 재고 증가
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }

    // 재고 감소

    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if (restStock<0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity=restStock;
    }

}
