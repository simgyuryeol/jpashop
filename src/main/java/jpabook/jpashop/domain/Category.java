package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "categroy_item",
            joinColumns = @JoinColumn(name = "category_id"), // 중간테이블에서의 id값
            inverseJoinColumns = @JoinColumn(name = "item_id") //이 테이블에서 item쪽으로 들어가는 값
    ) //중간테이블로 연결
    private List<Item> items= new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Category parent; //부모 계층 관계 부모는 나 자신임

    @OneToMany(mappedBy = "parent") //자식 계층관계 자식은 여러개를 가질수 있으므로 리스트
    private List<Category> child=new ArrayList<>();

    //양방향 연관관계 메서드
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
