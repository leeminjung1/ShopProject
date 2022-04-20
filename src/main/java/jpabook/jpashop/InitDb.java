package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("Choi", "1245 E 16th Street", "Wilmington", "Delaware", "19802");
            em.persist(member);

            Book book1 = createBook("참을 수 없는 존재의 가벼움", 13500, 100, "민음사", "밀란 쿤데라", "9788937437564");
            em.persist(book1);

            Book book2 = createBook("끝과 시작", 19800, 100, "문학과지성사", "비스와바 쉼보르스카", "9788932038711");
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 2);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("Kim", "100 Temple Ave.", "Hackensack", "New Jersey", "07601");
            em.persist(member);

            Book book1 = createBook("악의 꽃", 9000, 200, "민음사", "샤를 피에르 보들레르", "9788937475078");
            em.persist(book1);

            Book book2 = createBook("절대 돌아올 수 없는 것들", 10800, 300, "민음사", "에밀리 디킨슨", "9791196125752");
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 4);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }

        private Book createBook(String name, int price, int stockQuantity, String publisher, String author, String isbn) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            book.setPublisher(publisher);
            book.setAuthor(author);
            book.setIsbn(isbn);
            return book;
        }

        private Member createMember(String name, String street, String city, String state, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(street, city, state, zipcode));
            return member;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}
