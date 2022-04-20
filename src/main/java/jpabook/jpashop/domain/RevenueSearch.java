package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RevenueSearch {
    private String itemName;
    private String orderDateFrom;
    private String orderDateTo;
}
