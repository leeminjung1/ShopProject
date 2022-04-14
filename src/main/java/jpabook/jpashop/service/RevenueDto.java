package jpabook.jpashop.service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class RevenueDto {
    private Long itemId;
    private String itemName;
    private int price;
    private int count;
    private int total;
    private LocalDateTime dateTime;
}
