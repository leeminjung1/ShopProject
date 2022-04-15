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

    public RevenueDto(Long itemId, String itemName, int price, int count, int total, LocalDateTime dateTime) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.total = total;
        this.dateTime = dateTime;
    }
}
