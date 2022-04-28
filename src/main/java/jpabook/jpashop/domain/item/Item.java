package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
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

}
