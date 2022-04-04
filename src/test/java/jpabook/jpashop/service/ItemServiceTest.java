package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemService itemService;

    @Test
    void findOne() {
        Movie movie = new Movie();
        movie.setName("Fantastic Old-Fashioned");
        movie.setActor("Jannabi");
        movie.setPrice(20000);
        movie.setDirector("Peponi Music");
        itemService.saveItem(movie);
        Assertions.assertEquals(movie, itemRepository.findOne(movie.getId()));
    }
}