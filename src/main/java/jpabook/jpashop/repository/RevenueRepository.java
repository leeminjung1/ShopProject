package jpabook.jpashop.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.QItem;
import jpabook.jpashop.service.RevenueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jpabook.jpashop.domain.OrderStatus.CANCEL;

@Repository
@RequiredArgsConstructor
public class RevenueRepository {

    private final EntityManager em;

    /**
     * 매출 검색
     */
    public List<RevenueDto> findRevenueOld(RevenueSearch revenueSearch) {
        // JPQL
        String jpql = "select oi from OrderItem oi" +
                " left join fetch oi.item i" +
                " left join fetch oi.order o";

        boolean isFirst = false;

        //상품 이름 검색
        if (StringUtils.hasText(revenueSearch.getItemName())) {
            isFirst = true;
            jpql += " where" +
                    " i.name like :name";
        }

        if (StringUtils.hasText(revenueSearch.getOrderDateFrom())) {
            if (!isFirst) {
                jpql += " where";
            } else {
                jpql += " and";
            }
            isFirst = true;
            jpql += " o.orderDate >= :orderDateFrom";
        }

        if (StringUtils.hasText(revenueSearch.getOrderDateTo())) {
            if (!isFirst) {
                jpql += " where";
            } else {
                jpql += " and";
            }
            jpql += " o.orderDate < :orderDateTo";
        }

        TypedQuery<OrderItem> query = em.createQuery(jpql, OrderItem.class);

        if (StringUtils.hasText(revenueSearch.getItemName())) {
            query = query.setParameter("name", "%" + revenueSearch.getItemName() + "%");
        }
        if (StringUtils.hasText(revenueSearch.getOrderDateFrom())) {
            query = query.setParameter("orderDateFrom", LocalDateTime.parse(revenueSearch.getOrderDateFrom()));
        }
        if (StringUtils.hasText(revenueSearch.getOrderDateTo())) {
            query = query.setParameter("orderDateTo", LocalDateTime.parse(revenueSearch.getOrderDateTo()).plusMinutes(1));
        }

        return orderItemToRevenueDto(query.getResultList());
    }

    public List<RevenueDto> findRevenue(RevenueSearch revenueSearch) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QOrderItem oi = QOrderItem.orderItem;
        QOrder o = QOrder.order;
        QItem i = QItem.item;
        return orderItemToRevenueDto(query
                .select(oi)
                .from(oi)
                .join(oi.order, o)
                .join(oi.item, i)
                .where(itemNameLike(revenueSearch.getItemName()))
                .where(dateAfter(revenueSearch.getOrderDateFrom()))
                .where(dateBefore(revenueSearch.getOrderDateTo()))
                .fetch());
    }

    private BooleanExpression dateAfter(String orderDateFrom) {
        if (StringUtils.hasText(orderDateFrom)) {
            return QOrder.order.orderDate.after(LocalDateTime.parse(orderDateFrom));
        }
        return null;
    }

    private BooleanExpression dateBefore(String orderDateTo) {
        if (StringUtils.hasText(orderDateTo)) {
            return QOrder.order.orderDate.before(LocalDateTime.parse(orderDateTo));
        }
        return null;
    }

    private BooleanExpression itemNameLike(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return QItem.item.name.contains(itemName);
        }
        return null;
    }

    private List<RevenueDto> orderItemToRevenueDto(List<OrderItem> orderItems) {
        List<RevenueDto> list = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            Order order = orderItem.getOrder();
            if (order.getStatus() == CANCEL) {
                continue;
            }
            list.add(new RevenueDto(item.getId(), item.getName(), orderItem.getOrderPrice(), orderItem.getCount(), orderItem.getOrderPrice() * orderItem.getCount(), order.getOrderDate()));
        }
        return list;
    }
}
