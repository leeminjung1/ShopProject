package jpabook.jpashop.web;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class BookForm {

    private Long id;

    private List<String> itemCategory = new ArrayList<>();
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

    public BookForm() {
        itemCategory.add("Album");
        itemCategory.add("Book");
        itemCategory.add("Movie");
    }
}