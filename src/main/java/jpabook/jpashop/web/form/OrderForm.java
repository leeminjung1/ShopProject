package jpabook.jpashop.web.form;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderForm {
    private Long memberId;
    private Long itemId;
    private int count;
}
