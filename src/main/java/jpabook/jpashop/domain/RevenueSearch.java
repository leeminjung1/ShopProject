package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class RevenueSearch {
    private String itemName;
    private LocalDateTime orderDate;
}
