package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("book1", 10000, 100);
        int count = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), count);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야한다.");
        assertEquals(10000 * count, getOrder.getTotalPrice(), "주문가격은 가격*수량이다.");
        assertEquals(100 - count, item.getStockQuantity(), "주문 수량만큼 재고가 줄어야한다.");
    }
    
    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("book", 10000, 100);
        int count = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), count);

        // when
        orderService.cancelOrder(orderId);
        
        // then
        assertEquals(OrderStatus.CANCEL, orderRepository.findOne(orderId).getStatus(), "주문 상태가 CANCEL");
        assertEquals(100, book.getStockQuantity(), "주문하기 전과 재고가 같아야한다.");
    }
    
    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        int count = 100;
        Book book = createBook("book", 10000, count);

        // when
        orderService.order(member.getId(), book.getId(), count + 1);

        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }
}